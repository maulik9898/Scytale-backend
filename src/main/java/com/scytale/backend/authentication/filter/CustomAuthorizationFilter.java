package com.scytale.backend.authentication.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scytale.backend.authentication.utility.Token;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import static com.scytale.backend.authentication.utility.Constants.TOKEN_ACCESS;
import static com.scytale.backend.authentication.utility.Constants.TOKEN_REFRESH;
import static com.scytale.backend.authentication.utility.Constants.TOKEN_TYPE;
import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    //TODO ; add way to check if user still in database
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //TODO: add signup path for bypass
        if (request.getServletPath().equals("/auth/login") || request.getServletPath().equals("/token/refresh")) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String token = authorizationHeader.substring("Bearer ".length());

                    DecodedJWT decodedJWT = Token.decodedJWT(token);
                    String username = decodedJWT.getSubject();
                    String token_type = decodedJWT.getClaim(TOKEN_TYPE).asString();
                    if (!token_type.equals(TOKEN_ACCESS)) {
                        throw new RuntimeException("Token is not Access token");
                    }
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(s -> authorities.add(new SimpleGrantedAuthority(s)));
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception e){
                    log.error("Error logging in : {}",e.getMessage());
                    Map<String,String> error = new HashMap<>();
                    error.put("error_msg",e.getMessage());
                    response.setStatus(FORBIDDEN.value());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(),error);
                }

            }
            else{
                filterChain.doFilter(request,response);
            }
        }

    }
}
