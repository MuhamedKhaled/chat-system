package com.chat.elasticsearch.repositories;

import com.chat.elasticsearch.entities.MessageDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageSearchRepository extends ElasticsearchRepository<MessageDocument, String> {

    @Query("""
    {
      "bool": {
        "must": [
          { "term": { "chatId": ?0 } },
          { "wildcard": { "body": { "value": *?1* } } }
        ]
      }
    }
    """)
    List<MessageDocument> findMessagesByChatIdAndBody(Long chatId, String body, Pageable pageable);

}
