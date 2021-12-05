package com.scytale.backend.authentication.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scytale.backend.authentication.model.ERole;
import com.scytale.backend.authentication.model.Role;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import static com.scytale.backend.authentication.utility.Constants.TOKEN_ACCESS;
import static com.scytale.backend.authentication.utility.Constants.TOKEN_REFRESH;
import static com.scytale.backend.authentication.utility.Constants.TOKEN_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@Component
public class Token {

    public static Algorithm getAlgorithm() {
        return Algorithm.HMAC256(Constants.JWT_SECRET.getBytes());
    }

    public static void getTokenResponseBody(HttpServletResponse response, String refresh_token, String access_token) throws IOException {
        Map<String, String> body = new HashMap<>();
        body.put("access_token", access_token);
        body.put("refresh_token", refresh_token);
        body.put("created_at", new Date(System.currentTimeMillis()).toString());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }

    public static String getAccessToken(User user, HttpServletRequest request) {


        return JWT.create().withSubject(user.getUsername()).withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000)).withIssuer(request.getRequestURL().toString()).withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).withClaim(TOKEN_TYPE, TOKEN_ACCESS).sign(getAlgorithm());
    }

    public static String getAccessToken(com.scytale.backend.authentication.model.User user, HttpServletRequest request) {
        return JWT.create().withSubject(user.getUsername())

                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000)).withIssuer(request.getRequestURL().toString()).withClaim("roles", user.getRoles().stream().map(Role::getName).map(ERole::name).collect(Collectors.toList())).withClaim(TOKEN_TYPE, TOKEN_ACCESS).sign(getAlgorithm());
    }

    public static String getRefreshToken(User user, HttpServletRequest request) {

        return JWT.create().withSubject(user.getUsername())


                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)).withIssuer(request.getRequestURL().toString()).withClaim(TOKEN_TYPE, TOKEN_REFRESH).sign(getAlgorithm());
    }

    public static DecodedJWT decodedJWT(String token) {
        Algorithm algorithm = Token.getAlgorithm();
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);

    }
}
