package com.scytale.backend.websocket.repo;

import com.scytale.backend.websocket.model.DeviceSession;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceSessionRepo extends JpaRepository<DeviceSession, String> {
    DeviceSession findBySessionId(String sessionId);
}
