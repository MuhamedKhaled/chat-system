package com.chat.api.controllers.resources;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageResponse {
    private Integer number;
    private String message;
}
