package com.chat.data.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO implements Serializable {
    @JsonProperty("chat_id")
    private Long chatId;
    @JsonProperty("number")
    private Integer number;
    @JsonProperty("body")
    private String body;
}
