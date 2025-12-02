package br.gov.sp.fatec.pg.oo.controller;

import java.util.Map;

import br.gov.sp.fatec.pg.oo.service.ChatService;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class ChatController {

    private final ChatService chatService;

    public ChatController(Javalin app) {
        this.chatService = new ChatService();
        registerRoutes(app);
    }

    private void registerRoutes(Javalin app) {
        app.post("/chat", this::askChatbot);
    }

    private void askChatbot(Context ctx) {
        try {
            Map<String, String> body = ctx.bodyAsClass(Map.class);
            String question = body.get("message");

            String answer = chatService.ask(question);

            ctx.json(Map.of("response", answer));

        } catch (Exception e) {
            ctx.status(400).json(Map.of("error", e.getMessage()));
        }
    }
}
