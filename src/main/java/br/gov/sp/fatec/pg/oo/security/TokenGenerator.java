package br.gov.sp.fatec.pg.oo.security;

import java.util.UUID;

public class TokenGenerator {
    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
    }
}
