package com.chat.services;

import com.chat.api.controllers.resources.ChatResponse;
import com.chat.data.dtos.ChatMessageDTO;
import com.chat.data.entities.Chat;
import com.chat.data.repositories.ChatRepository;
import com.chat.mappers.ChatMapper;
import lombok.AllArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ChatService {
    private static final String  QUEUE_NAME ="chat_queue";
    private final ChatRepository chatRepository;
    private final ApplicationService applicationService;
    private final ChatMapper chatMapper;
    private final PublisherService publisherService;


    public List<ChatResponse> getAllChats(String appToken, int page, int limit) {
        Long applicationId = applicationService.getApplicationIdByToken(appToken);
        Pageable pageable = PageRequest.of(page, limit);
        Page<Chat> chatPage = chatRepository.findByApplicationId(applicationId, pageable);
        return chatMapper.toChatResponse(chatPage.getContent());
    }

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


}
