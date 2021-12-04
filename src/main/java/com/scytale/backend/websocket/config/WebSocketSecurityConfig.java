package com.scytale.backend.websocket.config;

import com.scytale.backend.authentication.model.ERole;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;


@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages.nullDestMatcher().hasAnyAuthority(ERole.ROLE_ADMIN.name(), ERole.ROLE_USER.name());
        messages.simpDestMatchers("/scytale-ws/**", "/topic/**", "/user/**", "/app/**", "/queue/**").hasAnyAuthority(ERole.ROLE_ADMIN.name(), ERole.ROLE_USER.name());
        messages.anyMessage().denyAll();

    }

    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }

}