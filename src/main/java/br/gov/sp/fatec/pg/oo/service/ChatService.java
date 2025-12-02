package br.gov.sp.fatec.pg.oo.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ChatService {

    private final String apiKey;
    private final HttpClient client;
    private final String model;

    public ChatService() {
        this.apiKey = System.getenv("OPENAI_API_KEY");
        if (this.apiKey == null || this.apiKey.isBlank()) {
            System.err.println("WARNING: OPENAI_API_KEY is not set");
        }
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        // Allow overriding model via env var, default to gpt-3.5-turbo (mais estável)
        String m = System.getenv("OPENAI_MODEL");
        this.model = (m == null || m.isBlank()) ? "gpt-3.5-turbo" : m;
    }

    public String ask(String userMessage) {
        if (this.apiKey == null || this.apiKey.isBlank()) {
            return "Erro ao chamar IA: OPENAI_API_KEY não configurada";
        }

        try {
            JsonObject body = new JsonObject();
            body.addProperty("model", this.model);

            // Monta messages no formato compatível (content como array de objetos com type+text)
            JsonArray messages = new JsonArray();

            JsonObject systemMsg = new JsonObject();
            systemMsg.addProperty("role", "system");
            JsonArray systemContent = new JsonArray();
            JsonObject systemText = new JsonObject();
            systemText.addProperty("type", "text");
            systemText.addProperty("text", "Você é o assistente HabitFlow e ajuda usuários a manter hábitos.");
            systemContent.add(systemText);
            systemMsg.add("content", systemContent);
            messages.add(systemMsg);

            JsonObject userMsg = new JsonObject();
            userMsg.addProperty("role", "user");
            JsonArray userContent = new JsonArray();
            JsonObject userText = new JsonObject();
            userText.addProperty("type", "text");
            userText.addProperty("text", userMessage);
            userContent.add(userText);
            userMsg.add("content", userContent);
            messages.add(userMsg);

            body.add("messages", messages);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.openai.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .timeout(Duration.ofSeconds(30))
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();
            String respBody = response.body();

            // DEBUG: sempre imprime o corpo para ajudar a entender porque choices é nulo
            System.out.println("OpenAI response status: " + status);
            System.out.println("OpenAI response body: " + respBody);

            // Se status não 2xx, tenta extrair mensagem de erro do JSON
            if (status < 200 || status >= 300) {
                try {
                    JsonObject err = JsonParser.parseString(respBody).getAsJsonObject();
                    if (err.has("error")) {
                        JsonElement e = err.get("error");
                        String msg = e.isJsonObject() && e.getAsJsonObject().has("message")
                                ? e.getAsJsonObject().get("message").getAsString()
                                : e.toString();
                        return "Erro ao chamar IA: " + msg + " (status " + status + ")";
                    }
                } catch (Exception ex) {
                    // parsing do error falhou -> retorna corpo cru
                    return "Erro ao chamar IA: status=" + status + " body=" + respBody;
                }
                return "Erro ao chamar IA: status=" + status + " body=" + respBody;
            }

            // Parse seguro: tenta vários formatos possíveis
            JsonObject json = JsonParser.parseString(respBody).getAsJsonObject();

            // Caso a resposta contenha "error" no corpo mesmo com 200
            if (json.has("error")) {
                JsonObject e = json.getAsJsonObject("error");
                String msg = e.has("message") ? e.get("message").getAsString() : e.toString();
                return "Erro ao chamar IA: " + msg;
            }

            // 1) padrão esperado: choices -> choices[0].message.content (pode ser array)
            if (json.has("choices") && json.get("choices").isJsonArray()) {
                JsonArray choices = json.getAsJsonArray("choices");
                if (choices.size() > 0) {
                    JsonObject choice = choices.get(0).getAsJsonObject();

                    // (a) Novo formato: choice.message.content -> array
                    if (choice.has("message") && choice.get("message").isJsonObject()) {
                        JsonObject message = choice.getAsJsonObject("message");

                        if (message.has("content")) {
                            JsonElement contentElem = message.get("content");
                            // content como array
                            if (contentElem.isJsonArray()) {
                                JsonArray contentArr = contentElem.getAsJsonArray();
                                if (contentArr.size() > 0) {
                                    JsonElement first = contentArr.get(0);
                                    if (first.isJsonObject() && first.getAsJsonObject().has("text")) {
                                        return first.getAsJsonObject().get("text").getAsString();
                                    } else if (first.isJsonPrimitive()) {
                                        return first.getAsString();
                                    }
                                }
                            } else if (contentElem.isJsonPrimitive()) {
                                return contentElem.getAsString();
                            }
                        }
                    }

                    // (b) Formato antigo: choice.message.content (string) ou choice.text
                    if (choice.has("message") && choice.get("message").isJsonObject()) {
                        JsonObject msgObj = choice.getAsJsonObject("message");
                        if (msgObj.has("content") && msgObj.get("content").isJsonPrimitive()) {
                            return msgObj.get("content").getAsString();
                        }
                    }
                    if (choice.has("text") && choice.get("text").isJsonPrimitive()) {
                        return choice.get("text").getAsString();
                    }
                }
            }

            // 2) formato 'responses' (Responses API) - opcional
            if (json.has("output") && json.get("output").isJsonArray()) {
                JsonArray out = json.getAsJsonArray("output");
                if (out.size() > 0 && out.get(0).isJsonObject()) {
                    JsonObject first = out.get(0).getAsJsonObject();
                    if (first.has("content") && first.get("content").isJsonArray()) {
                        JsonArray cont = first.getAsJsonArray("content");
                        if (cont.size() > 0 && cont.get(0).isJsonObject()) {
                            JsonObject c0 = cont.get(0).getAsJsonObject();
                            if (c0.has("text")) return c0.get("text").getAsString();
                        }
                    }
                }
            }

            // Se chegou aqui: não encontramos texto legível
            return "Erro ao chamar IA: resposta em formato inesperado. Verificar console (body acima).";

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro ao chamar IA: " + e.getMessage();
        }
    }
}
