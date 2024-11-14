package com.chat.services;


import com.chat.data.entities.Message;
import com.chat.data.repositories.MessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Message createMessage(Message message) {
        // Business logic for message numbering can be added here
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByChatId(Long chatId) {
        return messageRepository.findByChatIdOrderByNumber(chatId);
    }

}
