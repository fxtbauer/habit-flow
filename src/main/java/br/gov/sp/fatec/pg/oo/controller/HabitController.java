package br.gov.sp.fatec.pg.oo.controller;

import java.util.List;

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
        app.post("/habits", this::createHabit);
        app.get("/habits/{userId}", this::getHabitsByUser);
        app.put("/habits/{id}", this::updateHabit);
        app.delete("/habits/{id}", this::deleteHabit);
        app.get("/admin/habits", this::getAllHabits);

    }

    private void getAllHabits(Context ctx) {
        List<Habit> habits = habitRepository.getAllHabits();
        ctx.json(habits);
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
            int userId = ctx.pathParamAsClass("userId", Integer.class).get();
            ctx.json(habitRepository.getHabitsByUser(userId));
        } catch (Exception e) {
            ctx.status(400).json("Erro ao buscar hábitos: " + e.getMessage());
        }
    }

    private void updateHabit(Context ctx) {
        try {
            int id = ctx.pathParamAsClass("id", Integer.class).get();
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
