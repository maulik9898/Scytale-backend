package com.scytale.backend.websocket.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSession {

    String id;

    String name;

    String userName;

    List<String> subscriptions;

}
