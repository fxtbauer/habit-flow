package br.gov.sp.fatec.pg.oo.controller;

import br.gov.sp.fatec.pg.oo.model.Habit;
import br.gov.sp.fatec.pg.oo.model.User;
import br.gov.sp.fatec.pg.oo.repository.HabitRepository;
import br.gov.sp.fatec.pg.oo.security.AuthMiddleware;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class HabitController {

    private final HabitRepository repo = new HabitRepository();

    public HabitController(Javalin app) {
        registerRoutes(app);
    }

    private void registerRoutes(Javalin app) {
        app.get("/habits", this::list);
        app.post("/habits", this::create);
        app.put("/habits/{id}", this::update);
        app.delete("/habits/{id}", this::delete);
        app.get("/admin/habits", this::getAllHabits);

    }

    private void list(Context ctx) {
        User user = AuthMiddleware.authenticate(ctx);
        if (user == null) { ctx.status(401); return; }

        ctx.json(repo.getHabitsByUser(user.getId()));
    }

    private void create(Context ctx) {
        User user = AuthMiddleware.authenticate(ctx);
        if (user == null) { ctx.status(401); return; }

        Habit h = ctx.bodyAsClass(Habit.class);
        h.setUserId(user.getId());

        repo.createHabit(h);
        ctx.status(201).json("Criado!");
    }

    private void update(Context ctx) {
        User user = AuthMiddleware.authenticate(ctx);
        if (user == null) { ctx.status(401); return; }

        int id = Integer.parseInt(ctx.pathParam("id"));
        Habit h = ctx.bodyAsClass(Habit.class);
        h.setId(id);
        h.setUserId(user.getId());

        repo.updateHabit(h);
        ctx.json("Atualizado!");
    }

    private void delete(Context ctx) {
        User user = AuthMiddleware.authenticate(ctx);
        if (user == null) { ctx.status(401); return; }

        repo.deleteHabit(Integer.parseInt(ctx.pathParam("id")));
        ctx.json("Removido!");
    }
    private void getAllHabits(Context ctx) {
    HabitRepository repo = new HabitRepository();
    ctx.json(repo.getAllHabits());
}

}