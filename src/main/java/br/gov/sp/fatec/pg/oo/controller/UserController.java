package br.gov.sp.fatec.pg.oo.controller;

import java.util.Map;

import br.gov.sp.fatec.pg.oo.model.User;
import br.gov.sp.fatec.pg.oo.repository.UserRepository;
import br.gov.sp.fatec.pg.oo.security.TokenGenerator;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class UserController {

    private final UserRepository repo = new UserRepository();

    public UserController(Javalin app) {
        registerRoutes(app);
    }

    private void registerRoutes(Javalin app) {
        app.post("/register", this::register);
        app.post("/login", this::login);
        app.get("/users", this::getAll);
    }

    private void register(Context ctx) {
        User user = ctx.bodyAsClass(User.class);
        repo.createUser(user);
        ctx.status(201).json("Usuário criado!");
    }

    private void login(Context ctx) {
        User data = ctx.bodyAsClass(User.class);
        User user = repo.findByUsername(data.getUsername());

        if (user == null)
            { ctx.json(Map.of("success", false, "message", "Usuário não encontrado")); return; }

        if (!user.getPassword().equals(data.getPassword()))
            { ctx.json(Map.of("success", false, "message", "Senha incorreta")); return; }

        String token = TokenGenerator.generate();
        repo.saveToken(user.getId(), token);

        ctx.json(Map.of(
                "success", true,
                "token", token,
                "id", user.getId(),
                "username", user.getUsername(),
                "role", user.getRole()
        ));
    }

    private void getAll(Context ctx) {
        ctx.json(repo.findAll());
    }
}