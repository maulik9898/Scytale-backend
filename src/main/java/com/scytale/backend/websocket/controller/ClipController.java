package com.scytale.backend.websocket.controller;

import com.scytale.backend.websocket.model.Clip;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@AllArgsConstructor
@Slf4j
public class ClipController {


    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/send/clip")
    public void sendClipToUser(Clip message) {
        String user = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        simpMessagingTemplate.convertAndSendToUser(user, "/queue/get/clip", message.getContent());
    }
}