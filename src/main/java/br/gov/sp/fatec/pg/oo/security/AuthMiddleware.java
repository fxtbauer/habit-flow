package br.gov.sp.fatec.pg.oo.security;

import br.gov.sp.fatec.pg.oo.model.User;
import br.gov.sp.fatec.pg.oo.repository.UserRepository;
import io.javalin.http.Context;

public class AuthMiddleware {

    private static final UserRepository repo = new UserRepository();

    public static User authenticate(Context ctx) {
        String token = ctx.header("Authorization");

        if (token == null || token.isEmpty())
            return null;

        return repo.findByToken(token);
    }
}
