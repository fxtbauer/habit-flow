const baseUrl = "http://localhost:7070";

// Fetch com token opcional
function fetchWithToken(url, options = {}, token = null) {
  const headers = { "Content-Type": "application/json" };
  if (token) headers["Authorization"] = token;
  return fetch(url, { ...options, headers });
}

// Parse seguro para evitar erro quando não vem JSON
async function parse(response) {
  try {
    return await response.json();
  } catch (e) {
    return { error: "Resposta não é JSON" };
  }
}

// Log estilizado
function log(text) {
  console.log("%c" + text, "color:#58a6ff;font-weight:bold");
}

async function runTests() {
  log("INICIANDO TESTES");

  //
  // 1. Registrar admin
  //
  log("1) Registrando admin...");
  let res = await fetchWithToken(`${baseUrl}/register`, {
    method: "POST",
    body: JSON.stringify({ username: "admin", password: "123" }),
  });
  if (res.status === 500) {
    log("Registro admin -> já existia (OK)");
  } else {
    log("Registro admin -> " + res.status);
  }

  //
  // 2. Login admin
  //
  log("2) Login admin...");
  res = await fetchWithToken(`${baseUrl}/login`, {
    method: "POST",
    body: JSON.stringify({ username: "admin", password: "123" }),
  });
  let data = await parse(res);
  log("Login admin -> " + res.status + " | " + JSON.stringify(data));

  const adminToken = data.token;
  const adminId = data.id;

  //
  // 3. Registrar usuário comum
  //
  log("3) Registrando user novo...");
  res = await fetchWithToken(`${baseUrl}/register`, {
    method: "POST",
    body: JSON.stringify({ username: "ruan", password: "123" }),
  });
  log("Registro user -> " + res.status);

  //
  // 4. Login user
  //
  log("4) Login user...");
  res = await fetchWithToken(`${baseUrl}/login`, {
    method: "POST",
    body: JSON.stringify({ username: "ruan", password: "123" }),
  });
  data = await parse(res);
  log("Login user -> " + res.status + " | " + JSON.stringify(data));

  const userToken = data.token;
  const userId = data.id;

  //
  // 5. Criar hábito
  //
  log("5) Criando hábito...");
  res = await fetchWithToken(
    `${baseUrl}/habits`,
    {
      method: "POST",
      body: JSON.stringify({
        userId: userId,
        name: "Estudar Java",
        completed: false,
      }),
    },
    userToken
  );
  log("Criar hábito -> " + res.status);

  //
  // 6. Listar hábitos (rota nova)
  //
  log("6) Listando hábitos...");
  res = await fetchWithToken(`${baseUrl}/habits`, {}, userToken);
  data = await parse(res);
  log("Hábitos -> " + JSON.stringify(data));
  const habitId = data[0]?.id;

  //
  // 7. Atualizar hábito
  //
  if (habitId) {
    log("7) Atualizando hábito...");
    res = await fetchWithToken(
      `${baseUrl}/habits/${habitId}`,
      {
        method: "PUT",
        body: JSON.stringify({
          id: habitId,
          userId: userId,
          name: "Estudar Java Atualizado",
          completed: true,
        }),
      },
      userToken
    );
    log("Update -> " + res.status);
  }

  //
  // 8. Deletar hábito
  //
  if (habitId) {
    log("8) Deletando hábito...");
    res = await fetchWithToken(
      `${baseUrl}/habits/${habitId}`,
      {
        method: "DELETE",
      },
      userToken
    );
    log("Delete -> " + res.status);
  }

  //
  // 9. Admin lista usuários
  //
  log("9) Admin listando usuários...");
  res = await fetchWithToken(`${baseUrl}/admin/users`, {}, adminToken);
  data = await parse(res);
  log("Users -> " + JSON.stringify(data));

  //
  // 10. Admin promove user
  //
  log("10) Promovendo user...");
  res = await fetchWithToken(
    `${baseUrl}/admin/promote/${userId}`,
    {
      method: "PUT",
    },
    adminToken
  );
  log("Promover -> " + res.status);

  //
  // 11. Admin deleta user
  //
  log("11) Deletando user...");
  res = await fetchWithToken(
    `${baseUrl}/admin/delete/${userId}`,
    {
      method: "DELETE",
    },
    adminToken
  );
  log("Delete user -> " + res.status);

  log("TESTES CONCLUÍDOS");
}

runTests().catch((err) => console.error("Erro nos testes:", err));
