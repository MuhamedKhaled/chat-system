package com.chat.consumers;

import com.chat.data.dtos.MessageDTO;
import com.chat.data.entities.Message;
import com.chat.data.repositories.MessageRepository;
import com.chat.services.RedisCacheService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class MessagesConsumer {
    private static final String  QUEUE_NAME ="create_message_queue";

    private final MessageRepository messageRepository;
    private final RedisCacheService redisCacheService;
    private final ObjectMapper objectMapper;

    @Transactional
    @RabbitListener(queues = QUEUE_NAME, ackMode = "MANUAL")
    public void consumeMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            // Parse the incoming message to ChatMessageDTO
            MessageDTO messageDTO = objectMapper.readValue(message, MessageDTO.class);

            Message messageObj = Message.builder()
                    .body(messageDTO.getBody())
                    .number(messageDTO.getNumber())
                    .chatId(messageDTO.getChatId())
                    .build();


            // Save the chat entity
            messageRepository.save(messageObj);

            // Add application ID to Redis
            redisCacheService.addToSet("updated_chats", messageDTO.getChatId().toString());

            // Acknowledge the message
            channel.basicAck(deliveryTag, false);
            System.out.println(" [x] Successfully Consumed: " + message);
        } catch (Exception e) {
            // Reject the message and requeue it
            channel.basicNack(deliveryTag, false, true);
            System.err.println(" [!] Failed to process message: " + message);
            throw new IllegalStateException("Error processing the message", e);
        }
    }
}
