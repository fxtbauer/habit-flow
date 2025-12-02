package br.gov.sp.fatec.pg.oo.controller;

import java.util.Map;

import br.gov.sp.fatec.pg.oo.model.User;
import br.gov.sp.fatec.pg.oo.repository.UserRepository;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class UserController {

    private final UserRepository userRepository;

    public UserController(Javalin app) {
        this.userRepository = new UserRepository();
        registerRoutes(app);
    }

     private void registerRoutes(Javalin app) {

        app.post("/register", this::registerUser);
        app.post("/login", this::loginUser);
        app.get("/users", this::getAllUsers);
    }

    private void registerUser(Context ctx) {
        try {
            User user = ctx.bodyAsClass(User.class);

            userRepository.createUser(user);

            ctx.status(201).json("Usuário criado com sucesso!");
        } catch (Exception e) {
            ctx.status(400).json("Erro ao criar usuário: " + e.getMessage());
        }
    }

   private void loginUser(Context ctx) {
    try {
        User loginData = ctx.bodyAsClass(User.class);

        User user = userRepository.findByUsername(loginData.getUsername());

        if (user == null) {
            ctx.json(Map.of("success", false, "message", "Usuário não encontrado"));
            return;
        }

        if (!user.getPassword().equals(loginData.getPassword())) {
            ctx.json(Map.of("success", false, "message", "Senha incorreta"));
            return;
        }

        ctx.json(Map.of("success", true));

    } catch (Exception e) {
        ctx.json(Map.of("success", false, "message", "Erro no login"));
    }
}






    private void getAllUsers(Context ctx) {
        try {
            ctx.json(userRepository.findAll());
        } catch (Exception e) {
            ctx.status(400).json("Erro ao listar usuários: " + e.getMessage());
        }
    }
}
