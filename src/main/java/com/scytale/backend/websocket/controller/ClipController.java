package com.scytale.backend.websocket.controller;

import com.scytale.backend.websocket.model.Clip;
import com.scytale.backend.websocket.model.DeviceSession;
import com.scytale.backend.websocket.model.UserSession;
import com.scytale.backend.websocket.service.DeviceSessionService;
import com.scytale.backend.websocket.utils.PubSubMapping;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.nonNull;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ClipController {


    private final SimpMessagingTemplate simpMessagingTemplate;

    private final DeviceSessionService deviceSessionService;
    private final SimpUserRegistry simpUserRegistry;

    public void sendSessionEvents(String userName) {

        UserSession userSession = new UserSession();
        if (nonNull(simpUserRegistry.getUser(userName))) {
            userSession.setUserName(userName);
            userSession.setSessions(simpUserRegistry.getUser(userName).getSessions().stream().map(simpSession -> {
                DeviceSession session = deviceSessionService.getDeviceSession(simpSession.getId());
                session.setSubscriptions(simpSession.getSubscriptions().stream().map(SimpSubscription::getDestination).collect(Collectors.toList()));
                return session;
            }).collect(Collectors.toList()));

        }
        simpMessagingTemplate.convertAndSendToUser(userName, PubSubMapping.SESSION_SUB, userSession);
    }

    @MessageMapping(PubSubMapping.KEY_PUB)
    public void updatePublicKey(String key, Principal principal, SimpMessageHeaderAccessor headerAccessor) {
        deviceSessionService.setPublicKey(headerAccessor.getSessionId(), key);
        sendSessionEvents(principal.getName());
    }

    @MessageMapping(PubSubMapping.CLIP_PUB)
    public void sendClipToUser(Clip message, Principal principal) {
        simpMessagingTemplate.convertAndSendToUser(principal.getName(), PubSubMapping.CLIP_SUB, message.getContent());
    }
}