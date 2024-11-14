package com.chat.elasticsearch.repositories;

import com.chat.elasticsearch.entities.MessageDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageSearchRepository extends ElasticsearchRepository<MessageDocument, String> {
    List<MessageDocument> findByBodyContaining(String text);
}
