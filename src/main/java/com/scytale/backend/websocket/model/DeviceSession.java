package com.scytale.backend.websocket.model;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceSession {

    @Id
    String sessionId;

    String deviceName;

    String userName;

    @ElementCollection
    List<String> subscriptions;

    String pubKey;

}
