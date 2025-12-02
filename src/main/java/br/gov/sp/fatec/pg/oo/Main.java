package br.gov.sp.fatec.pg.oo;

import br.gov.sp.fatec.pg.oo.controller.AdminController;
import br.gov.sp.fatec.pg.oo.controller.ChatController;
import br.gov.sp.fatec.pg.oo.controller.HabitController;
import br.gov.sp.fatec.pg.oo.controller.UserController;
import br.gov.sp.fatec.pg.oo.database.DatabaseInitializer;
import io.javalin.Javalin;

public class Main {

    public static void main(String[] args) {

        DatabaseInitializer.initialize();

        Javalin app = Javalin.create(config -> {
        config.staticFiles.add("static");
        }).start(7070);
        
        new UserController(app);
        new HabitController(app);
        new ChatController(app);
        new AdminController(app);

        app.get("/status", ctx -> ctx.result("API online"));
        app.get("/", ctx -> ctx.redirect("/login"));

        app.get("/login", ctx -> ctx.redirect("/login.html"));
        app.get("/dashboard", ctx -> ctx.redirect("/dashboard.html"));
        app.get("/admin", ctx -> ctx.redirect("/admin.html"));
    }
}
