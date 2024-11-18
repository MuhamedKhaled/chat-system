package com.chat.services;

import com.chat.api.controllers.resources.ChatResponse;
import com.chat.data.dtos.ChatMessageDTO;
import com.chat.data.entities.Chat;
import com.chat.data.repositories.ChatRepository;
import com.chat.data.repositories.MessageRepository;
import com.chat.mappers.ChatMapper;
import lombok.AllArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.redisson.api.RLock;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatService {
    private static final String  QUEUE_NAME ="create_chat_queue";
    private final ChatRepository chatRepository;
    private final ApplicationService applicationService;
    private final ChatMapper chatMapper;
    private final PublisherService publisherService;
    private final RedisCacheService redisCacheService;
    private final MessageRepository messageRepository;


    public List<ChatResponse> getAllChats(String appToken, int page, int limit) {
        Long applicationId = applicationService.getApplicationIdByToken(appToken);
        Pageable pageable = PageRequest.of(page, limit);
        Page<Chat> chatPage = chatRepository.findByApplicationId(applicationId, pageable);
        return chatMapper.toChatResponse(chatPage.getContent());
    }

    @Transactional
    public ChatResponse createChat(String appToken) {
        Long applicationId = applicationService.getApplicationIdByToken(appToken);
        Integer newChatNumber = applicationService.getNewChatNumber(applicationId, appToken);
        if (newChatNumber == null) {
            System.out.println("Failed to assign chat number");
            throw new RuntimeException("Failed to assign chat number");
        }
        ChatMessageDTO chatMessageDTO = ChatMessageDTO.builder().applicationId(applicationId).number(newChatNumber).build();
        publisherService.publish(QUEUE_NAME, chatMessageDTO);

        return  ChatResponse.builder().number(newChatNumber).messagesCount(0).build();
    }

    public Optional<Integer> findMaxNumberByApplicationId(Long applicationId){
        return chatRepository.findMaxNumberByApplicationId(applicationId);
    }

    public ChatResponse getChatByApplicationIdAndNumber(String appToken, Integer number) {
        Long applicationId = applicationService.getApplicationIdByToken(appToken);
        return chatRepository.findByApplicationIdAndNumber(applicationId, number)
                .map(chatMapper::toChatResponse)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
    }

    public Long getChatId(String appToken, Integer chatNumber) {
        Long applicationId = applicationService.getApplicationIdByToken(appToken);
        return chatRepository.findChatIdByApplicationIdAndChatNumber(applicationId, chatNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Chat not found"));
    }

    public Integer getNewMessageNumber(String applicationToken, Long chatId) {
        String redisKey = applicationToken + "_" + chatId + "_messages_count";
        String lockKey = redisKey + "_lock";

        // Use Redis atomic increment if the key exists
        if (redisCacheService.exists(redisKey)) {
            return redisCacheService.increment(redisKey);
        }

        // Fallback to lock mechanism
        return generateMessageNumberWithLock(chatId, redisKey, lockKey);
    }

    private Integer generateMessageNumberWithLock(Long chatId, String redisKey, String lockKey) {
        RLock lock = null;
        try {
            lock = redisCacheService.acquireLock(lockKey, 2, 5); // 2 seconds wait, 5 seconds lease time

            // Double-check if the key exists inside the lock
            int messagesInQueueMax = redisCacheService.get(redisKey) != null ? Integer.parseInt(redisCacheService.get(redisKey)) : 0;

            // Get the max message number from the database
            Integer messagesDbMax = messageRepository.findMaxNumberByChatId(chatId).orElse(0);

            // Calculate the new message number
            int newMessageNumber = Math.max(messagesInQueueMax, messagesDbMax) + 1;

            // Save the new number in Redis with an expiry
            redisCacheService.set(redisKey, String.valueOf(newMessageNumber), Duration.ofHours(1));

            return newMessageNumber;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread was interrupted while waiting for lock.", e);
        } finally {
            if (lock != null) {
                redisCacheService.releaseLock(lock);
            }
        }
    }
}
