package com.chat.api.controllers.resources;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatResponse {
    private Integer number;
    private Integer messagesCount;
}
