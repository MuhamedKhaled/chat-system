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
public class ChatMessageDTO implements Serializable {
    @JsonProperty("application_id")
    private Long applicationId;
    @JsonProperty("number")
    private Integer number;
}
