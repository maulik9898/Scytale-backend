package com.scytale.backend.websocket.interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.scytale.backend.authentication.utility.Token;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;

import static com.scytale.backend.authentication.utility.Constants.TOKEN_ACCESS;
import static com.scytale.backend.authentication.utility.Constants.TOKEN_TYPE;
import static java.util.Arrays.stream;


@Slf4j
@Component

public class StompChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        assert accessor != null;
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            List<String> authorizationHeaders = Objects.requireNonNullElse(accessor.getNativeHeader("Authorization"), new ArrayList<>());
            if (!authorizationHeaders.isEmpty() && authorizationHeaders.get(0).startsWith("Bearer ")) {
                try {
                    String token = authorizationHeaders.get(0).substring("Bearer ".length());

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

                    accessor.setUser(SecurityContextHolder.getContext().getAuthentication());
                    return message;
                } catch (Exception e) {
                    log.error("Error logging in : {}", e.getMessage());
                }


            }


        }
        return message;
    }
}
