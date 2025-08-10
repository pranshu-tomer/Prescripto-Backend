package com.prescripto.springBackend.util;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvUtil {

    private static final Dotenv dotenv = Dotenv.load();
    public static String getJwt_Secret() {
        return dotenv.get("JWT_SECRET");
    }
    public static String getCloudName() {
        return dotenv.get("CLOUDINARY_NAME");
    }
    public static String getCloudApiKey() {
        return dotenv.get("CLOUDINARY_API_KEY");
    }
    public static String getCloudSecretKey() {
        return dotenv.get("CLOUDINARY_SECRET_KEY");
    }
}
