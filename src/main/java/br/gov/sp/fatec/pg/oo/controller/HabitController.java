package br.gov.sp.fatec.pg.oo.controller;

import br.gov.sp.fatec.pg.oo.model.Habit;
import br.gov.sp.fatec.pg.oo.repository.HabitRepository;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class HabitController {

    private final HabitRepository habitRepository;

    public HabitController(Javalin app) {
        this.habitRepository = new HabitRepository();
        registerRoutes(app);
    }

    private void registerRoutes(Javalin app) {
        app.post("/habit", this::createHabit);
        app.get("/habit/:userId", this::getHabitsByUser);
        app.put("/habit/:id", this::updateHabit);
        app.delete("/habit/:id", this::deleteHabit);
    }

    private void createHabit(Context ctx) {
        try {
            Habit habit = ctx.bodyAsClass(Habit.class);
            habitRepository.createHabit(habit);
            ctx.status(201).json("Hábito criado com sucesso!");
        } catch (Exception e) {
            ctx.status(400).json("Erro ao criar hábito: " + e.getMessage());
        }
    }

    private void getHabitsByUser(Context ctx) {
        try {
            int userId = Integer.parseInt(ctx.pathParam("userId"));
            ctx.json(habitRepository.getHabitsByUser(userId));
        } catch (Exception e) {
            ctx.status(400).json("Erro ao buscar hábitos: " + e.getMessage());
        }
    }

    private void updateHabit(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Habit habit = ctx.bodyAsClass(Habit.class);
            habit.setId(id);

            habitRepository.updateHabit(habit);
            ctx.status(200).json("Hábito atualizado!");

        } catch (Exception e) {
            ctx.status(400).json("Erro ao atualizar hábito: " + e.getMessage());
        }
    }

    private void deleteHabit(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            habitRepository.deleteHabit(id);
            ctx.status(200).json("Hábito removido!");
        } catch (Exception e) {
            ctx.status(400).json("Erro ao deletar hábito: " + e.getMessage());
        }
    }
}
