package com.scytale.backend.websocket.listener;

import com.scytale.backend.websocket.model.DeviceSession;
import com.scytale.backend.websocket.model.UserSession;
import com.scytale.backend.websocket.service.DeviceSessionService;
import com.scytale.backend.websocket.utils.PubSubMapping;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpSubscription;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
@Slf4j
@RequiredArgsConstructor
public class StompEventListener {

    private final DeviceSessionService deviceSessionService;
    private final SimpUserRegistry simpUserRegistry;
    private final SimpMessagingTemplate simpMessagingTemplate;


    public void sendSessionEvents(String userName) {

        List<UserSession> userSessions = new ArrayList<>();
        if (nonNull(simpUserRegistry.getUser(userName))) {
            userSessions = simpUserRegistry.getUser(userName).getSessions().stream().map(simpSession -> new UserSession(simpSession.getId(), deviceSessionService.getDeviceName(simpSession.getId()), simpSession.getUser().getName(), simpSession.getSubscriptions().stream().map(SimpSubscription::getDestination).collect(Collectors.toList()))).collect(Collectors.toList());

        }
        simpMessagingTemplate.convertAndSendToUser(userName, PubSubMapping.SESSION_SUB, userSessions);
    }

    @EventListener
    public void handleSessionConnectedEvent(SessionConnectedEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String sessionId = accessor.getMessageHeaders().get(SimpMessageHeaderAccessor.SESSION_ID_HEADER, String.class);
        GenericMessage<?> generic = (GenericMessage<?>) accessor.getHeader(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER);
        String deviceName = sessionId;
        if (nonNull(generic)) {
            SimpMessageHeaderAccessor nativeAccessor = SimpMessageHeaderAccessor.wrap(generic);
            List<String> deviceNames = nativeAccessor.getNativeHeader("Name");

            deviceName = isNull(deviceNames) ? sessionId : deviceNames.stream().findFirst().orElse(sessionId);
        }
        deviceSessionService.saveDeviceSession(new DeviceSession(sessionId, deviceName, accessor.getUser().getName()));
        sendSessionEvents(accessor.getUser().getName());
        log.info("Device {} connected", deviceName);
    }

    @EventListener
    public void handleSessionSubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        if (accessor.getDestination().equals(PubSubMapping.USER_SESSION_SUB)) {
            sendSessionEvents(accessor.getUser().getName());

        }
        log.info("subscribe to {}", accessor.getDestination());
    }


    @EventListener
    public void handleSessionDisconnectedEvent(SessionDisconnectEvent event) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());
        DeviceSession session = deviceSessionService.deleteDeviceSession(stompHeaderAccessor.getSessionId());
        sendSessionEvents(session.getUserName());
        log.info(event.toString());
    }

}
