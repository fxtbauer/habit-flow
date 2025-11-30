package br.gov.sp.fatec.pg.oo.controller;

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
                ctx.status(404).json("Usuário não pode ser encontrado");
                return;
            }

            if (!user.getPassword().equals(loginData.getPassword())) {
                ctx.status(401).json("Senha incorreta");
                return;
            }

            ctx.status(200).json("Login bem-sucedido!");

        } catch (Exception e) {
            ctx.status(400).json("Erro no login: " + e.getMessage());
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
