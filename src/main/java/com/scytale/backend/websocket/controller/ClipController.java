package com.scytale.backend.websocket.controller;

import com.scytale.backend.websocket.model.Clip;
import com.scytale.backend.websocket.utils.PubSubMapping;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ClipController {


    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping(PubSubMapping.CLIP_PUB)
    public void sendClipToUser(Clip message, Principal principal) {
        simpMessagingTemplate.convertAndSendToUser(principal.getName(), PubSubMapping.CLIP_SUB, message.getContent());
    }
}