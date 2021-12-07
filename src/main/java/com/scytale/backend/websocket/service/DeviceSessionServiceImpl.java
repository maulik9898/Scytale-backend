package com.scytale.backend.websocket.service;

import com.scytale.backend.websocket.model.DeviceSession;
import com.scytale.backend.websocket.repo.DeviceSessionRepo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DeviceSessionServiceImpl implements DeviceSessionService {
    private final DeviceSessionRepo deviceSessionRepo;

    @Override
    public DeviceSession saveDeviceSession(DeviceSession deviceSession) {

        return deviceSessionRepo.save(deviceSession);
    }

    @Override
    public DeviceSession deleteDeviceSession(String sessionId) {
        DeviceSession session = deviceSessionRepo.findBySessionId(sessionId);
        deviceSessionRepo.deleteById(sessionId);
        return session;

    }

    @Override
    public String getDeviceName(String sessionId) {
        return deviceSessionRepo.findBySessionId(sessionId).getDeviceName();
    }

    @Override
    public void setPublicKey(String sessionId, String key) {
        DeviceSession session = deviceSessionRepo.findBySessionId(sessionId);
        session.setPubKey(key);
    }

    @Override
    public DeviceSession getDeviceSession(String sessionId) {
        return deviceSessionRepo.findBySessionId(sessionId);
    }
}
