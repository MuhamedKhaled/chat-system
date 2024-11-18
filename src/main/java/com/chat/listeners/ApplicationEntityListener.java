package com.chat.listeners;

import com.chat.data.entities.Application;
import com.chat.utils.TokenConverter;
import jakarta.persistence.PrePersist;

import java.util.UUID;

public class ApplicationEntityListener {
    @PrePersist
    public void generateToken(Application application) {
        if (application.getToken() == null) {
            application.setToken(TokenConverter.toBytes(UUID.randomUUID().toString()));
        }
    }


}
