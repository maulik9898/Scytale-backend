package com.scytale.backend.websocket.model;

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

}
