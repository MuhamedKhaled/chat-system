package com.chat.api.controllers.resources;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

@Data
public class ApplicationRequest {
    @NotBlank(message = "Name is required")
    @NotNull
    private String name;
}
