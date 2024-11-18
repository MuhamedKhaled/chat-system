package com.chat.services;

import com.chat.api.controllers.resources.MessageResponse;
import com.chat.elasticsearch.entities.MessageDocument;
import com.chat.elasticsearch.repositories.MessageSearchRepository;
import com.chat.mappers.MessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ElasticsearchService {

    @Autowired
    MessageSearchRepository messageSearchRepository;
    @Autowired
    MessageMapper messageMapper;

    public List<MessageResponse> searchMessages(Long chatId, String body, Pageable pageable) {
        List<MessageDocument> messageDocuments = messageSearchRepository.findMessagesByChatIdAndBody(chatId, body, pageable);
        return messageMapper.toMessageResponse(messageDocuments);
    }
}
