package br.gov.sp.fatec.pg.oo;

import br.gov.sp.fatec.pg.oo.database.DatabaseInitializer;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        System.out.println("Teste inicial");

        DatabaseInitializer.initialize();
        Javalin app = Javalin.create( config -> {
        config.http.defaultContentType = "text/plain; charset = UTF-8";
        config.pvt.javaLangErrorHandler((res, error) ->  {
            res.setCharacterEncoding("UTF-8");
        });
        }).start(7070);
        app.get("/status", ctx ->  { 
            // Teste
            ctx.result("API online");
        });
    }
}