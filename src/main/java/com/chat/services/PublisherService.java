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
    private static final boolean DURABLE = true;

    private final RabbitTemplate rabbitTemplate;
    private final AmqpAdmin amqpAdmin;
    private final ObjectMapper objectMapper;

    public void publish(String queueName, Object object) {
        String payload = serializeToJson(object);
        String exchangeName = queueName + "_exchange";

        // Declare exchange
        Exchange exchange = ExchangeBuilder.directExchange(exchangeName)
                .durable(DURABLE)
                .build();
        amqpAdmin.declareExchange(exchange);

        // Declare queue
        Queue queue = QueueBuilder.durable(queueName).build();
        amqpAdmin.declareQueue(queue);

        // Bind queue to exchange
        Binding binding = BindingBuilder.bind(queue)
                .to((DirectExchange) exchange)
                .with(queue.getName());
        amqpAdmin.declareBinding(binding);

        // Publish message
        rabbitTemplate.convertAndSend(exchangeName, queue.getName(), payload);
    }

    private String serializeToJson(Object payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize ChatMessageDTO to JSON", e);
        }
    }
}
