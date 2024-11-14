package com.chat.services;

import com.chat.data.entities.Chat;
import com.chat.data.repositories.ChatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;

    public Chat createChat(Chat chat) {
        // Business logic for chat numbering can go here
        return chatRepository.save(chat);
    }

    public Chat getChatByApplicationIdAndNumber(Long applicationId, Integer number) {
        return chatRepository.findByApplicationIdAndNumber(applicationId, number)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
    }
}
