package com.prescripto.springBackend.util;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvUtil {

    private static final Dotenv dotenv = Dotenv.load();
    public static String getJwt_Secret() {
        return dotenv.get("JWT_SECRET");
    }
}
