package br.gov.sp.fatec.pg.oo.controller;

import java.util.Map;

import br.gov.sp.fatec.pg.oo.repository.UserRepository;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class AdminController {

    private final UserRepository userRepository;

    public AdminController(Javalin app) {
        this.userRepository = new UserRepository();
        registerRoutes(app);
    }

    private void registerRoutes(Javalin app) {

 
        app.get("/admin/users", this::getAll);

        app.put("/admin/promote/{id}", this::promoteUser);

        app.delete("/admin/delete/{id}", this::deleteUser);
    }

    private void getAll(Context ctx) {
        ctx.json(userRepository.findAll());
    }

    private void promoteUser(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).get();
        userRepository.promoteToAdmin(id);

        ctx.json(Map.of("success", true));
    }

    private void deleteUser(Context ctx) {
        int id = ctx.pathParamAsClass("id", Integer.class).get();
        userRepository.deleteUser(id);

        ctx.json(Map.of("success", true));
    }
}
