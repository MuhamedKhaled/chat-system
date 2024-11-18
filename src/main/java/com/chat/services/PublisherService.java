package com.chat.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublisherService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public void publish(String queueName, Object object) {
        String payload = serializeToJson(object);
        String exchangeName = queueName + "_exchange";
        // Publish message
        rabbitTemplate.convertAndSend(exchangeName, queueName, payload);
    }

    private String serializeToJson(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize ChatMessageDTO to JSON", e);
        }
    }
}
