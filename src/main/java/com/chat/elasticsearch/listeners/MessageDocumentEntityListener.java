package com.chat.elasticsearch.listeners;

import com.chat.data.entities.Message;
import com.chat.elasticsearch.entities.MessageDocument;
import com.chat.elasticsearch.repositories.MessageSearchRepository;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreRemove;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageDocumentEntityListener {

    @Autowired
    private MessageSearchRepository messageSearchRepository;

    @PostPersist
    @PostUpdate
    public void syncMessageToElasticsearch(Message message) {
        MessageDocument document = new MessageDocument(message.getChatId(), message.getNumber(), message.getBody());
        messageSearchRepository.save(document);
    }

    @PreRemove
    public void deleteMessageFromElasticsearch(Message message) {
        messageSearchRepository.deleteById(message.getId().toString());
    }
}
