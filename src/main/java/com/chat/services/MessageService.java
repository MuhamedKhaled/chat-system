package com.chat.services;


import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import com.chat.data.entities.Message;
import com.chat.data.repositories.MessageRepository;
import com.chat.elasticsearch.repositories.MessageSearchRepository;
import lombok.AllArgsConstructor;

import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageSearchRepository messageSearchRepository;


    public Message saveMessage(Integer number, Long chatId, String body) {
        Message message = new Message();
        message.setNumber(number);
        message.setChatId(chatId);
        message.setBody(body);
        messageSearchRepository.findMessagesByChatIdAndBody(chatId,"te");
        return messageRepository.save(message);
    }

    public Message createMessage(Message message) {
        // Business logic for message numbering can be added here
        return messageRepository.save(message);
    }

    public List<Message> getMessagesByChatId(Long chatId) {
        return messageRepository.findByChatIdOrderByNumber(chatId);
    }


}
