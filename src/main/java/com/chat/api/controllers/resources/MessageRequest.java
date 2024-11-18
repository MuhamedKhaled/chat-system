package com.chat.api.controllers.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MessageRequest {
    @NotBlank(message = "body is required")
    @NotNull
    private String body;
}
