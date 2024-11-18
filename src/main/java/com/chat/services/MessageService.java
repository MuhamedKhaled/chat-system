package com.chat.services;


import com.chat.api.controllers.resources.MessageRequest;
import com.chat.api.controllers.resources.MessageResponse;
import com.chat.data.dtos.ChatMessageDTO;
import com.chat.data.dtos.MessageDTO;
import com.chat.data.repositories.MessageRepository;
import com.chat.mappers.MessageMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {
    private static final String  QUEUE_NAME ="create_message_queue";
    private final MessageRepository messageRepository;
    private final ElasticsearchService elasticsearchService;
    private final MessageMapper messageMapper;
    private final ChatService chatService;
    private final PublisherService publisherService;

    public MessageResponse createMessage(String appToken, Integer chatNumber, MessageRequest request) {
        Long chatId = chatService.getChatId(appToken,chatNumber);
        Integer newMessageNumber = chatService.getNewMessageNumber(appToken,chatId);
        if (newMessageNumber == null) {
            System.out.println("Failed to assign message number");
            throw new RuntimeException("Failed to assign message number");
        }
        MessageDTO messageDTO = MessageDTO.builder().chatId(chatId).number(newMessageNumber).body(request.getBody()).build();
        publisherService.publish(QUEUE_NAME, messageDTO);
        return MessageResponse.builder().message(request.getBody()).number(newMessageNumber).build();
    }


    public List<MessageResponse> listMessages(String appToken, Integer number, String query, Pageable pageable) {
        Long chatId = chatService.getChatId(appToken,number);
        if (query != null && !query.isEmpty()) {
            // Search in Elasticsearch
            return elasticsearchService.searchMessages(chatId, query, pageable);
        } else {
            // Fetch directly from the database
            return messageMapper.mapEntityToResponse(messageRepository.findByChatId(chatId, pageable).getContent());
        }
    }

}
