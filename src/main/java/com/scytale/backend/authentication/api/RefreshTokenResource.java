package com.scytale.backend.authentication.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scytale.backend.authentication.model.User;
import com.scytale.backend.authentication.service.UserService;
import com.scytale.backend.authentication.utility.Token;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.scytale.backend.authentication.utility.Constants.TOKEN_REFRESH;
import static com.scytale.backend.authentication.utility.Constants.TOKEN_TYPE;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class RefreshTokenResource {

    private final UserService userService;

    @GetMapping("/refresh")
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());

                DecodedJWT decodedJWT = Token.decodedJWT(refresh_token);
                String username = decodedJWT.getSubject();
                String token_type = decodedJWT.getClaim(TOKEN_TYPE).asString();
                if (!token_type.equals(TOKEN_REFRESH)) {
                    throw new RuntimeException("Token is not refresh token");
                }
                User user = userService.getUser(username);

                String access_token = Token.getAccessToken(user, request);

                Token.getTokenResponseBody(response, refresh_token, access_token);


            } catch (Exception e) {
                log.error("Error  in : {}", e.getMessage());
                Map<String, String> error = new HashMap<>();
                error.put("error_msg", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }

    }


}
