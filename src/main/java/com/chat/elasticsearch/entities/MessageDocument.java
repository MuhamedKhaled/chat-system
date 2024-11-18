package com.chat.elasticsearch.entities;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Data
@Setting(settingPath = "/elastic/elasticsearch-settings.json")
@Document(indexName = "chat_messages")
public class MessageDocument {
    @Id
    private String id;

    @Field(type = FieldType.Long)
    private Long chatId;

    @Field(type = FieldType.Integer)
    private Integer number;

    @Field(type = FieldType.Text, analyzer = "ngram_analyzer")
    private String body;

    public MessageDocument(Long chatId, Integer number, String body) {
        this.chatId = chatId;
        this.number = number;
        this.body = body;
    }
}
