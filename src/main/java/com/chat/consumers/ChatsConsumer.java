package com.chat.consumers;

import com.chat.data.dtos.ChatMessageDTO;
import com.chat.data.entities.Chat;
import com.chat.data.repositories.ChatRepository;
import com.chat.services.RedisCacheService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.messaging.handler.annotation.Header;
import com.rabbitmq.client.Channel;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ChatsConsumer {
    private static final String  QUEUE_NAME ="chat_queue";

    private final ChatRepository chatRepository;
    private final RedisCacheService redisCacheService;
    private final ObjectMapper objectMapper;

    @Transactional
    @RabbitListener(queues = QUEUE_NAME, ackMode = "MANUAL")
    public void consumeMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        try {
            // Parse the incoming message to ChatMessageDTO
            ChatMessageDTO chatMessage = objectMapper.readValue(message, ChatMessageDTO.class);

            Chat chat = Chat.builder()
                    .applicationId(chatMessage.getApplicationId())
                    .number(chatMessage.getNumber())
                    .messagesCount(0)
                    .build();

            // Save the chat entity
            chatRepository.save(chat);

            // Add application ID to Redis
            redisCacheService.addToSet("updated_applications", chatMessage.getApplicationId().toString());

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
