package com.scytale.backend.authentication.utility;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Constants {
    public static final String TOKEN_TYPE = "TOKEN_TYPE";
    public static final String TOKEN_ACCESS = "TOKEN_ACCESS";
    public static final String TOKEN_REFRESH = "TOKEN_REFRESH";

    //TODO: use environment variable
    public static final String JWT_SECRET = "63CF48BBB53C8866579D8417F63BDA08FD289166AEB041C7FD94884BC6BCF21C";
}
