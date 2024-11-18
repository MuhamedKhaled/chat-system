package com.chat.configs;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RabbitMQConfig {
    private static final String [] queuesArray ={"create_chat_queue","create_message_queue"};
    private static final List<String> queues = new ArrayList<>(Arrays.asList(queuesArray));
    private static final boolean DURABLE = true;

    private final AmqpAdmin amqpAdmin;

    public void declareQueueWithExchange(String queueName) {
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

        System.out.println("Queue, Exchange, and Binding created for: " + queueName);
    }
    /**
     * Initializes required queues and exchanges at application startup.
     */
    @PostConstruct
    public void setupQueuesOnStartup() {

        if (!queues.isEmpty()) {
            queues.forEach(this::declareQueueWithExchange);
        } else {
            System.out.println("No queues configured to initialize at startup.");
        }
    }

}
