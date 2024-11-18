package com.chat.api.controllers.resources;

import lombok.Data;

@Data
public class ApplicationResponse {
    private String token;
    private String name;
    private int chatsCount;

}
