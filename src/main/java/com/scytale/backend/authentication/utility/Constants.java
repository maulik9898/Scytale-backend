package com.scytale.backend.authentication.utility;


import lombok.Data;


@Data
public class Constants {
    public static final String TOKEN_TYPE = "TOKEN_TYPE";
    public static final String TOKEN_ACCESS = "TOKEN_ACCESS";
    public static final String TOKEN_REFRESH = "TOKEN_REFRESH";

    // in minutes
    public static final Integer ACCESS_TOKEN_VALIDITY = 30;

    //in hours
    public static final Integer REFRESH_TOKEN_VALIDITY = 24;


    public static String JWT_SECRET;

    public void setJwtSecret(String jwtSecret) {
        Constants.JWT_SECRET = jwtSecret;
    }

}
