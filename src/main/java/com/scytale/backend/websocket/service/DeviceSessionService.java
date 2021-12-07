package com.scytale.backend.websocket.service;

import com.scytale.backend.websocket.model.DeviceSession;

public interface DeviceSessionService {

    DeviceSession saveDeviceSession(DeviceSession deviceSession);

    DeviceSession deleteDeviceSession(String sessionId);

    String getDeviceName(String sessionId);

    void setPublicKey(String sessionId, String key);

    DeviceSession getDeviceSession(String sessionId);

}
