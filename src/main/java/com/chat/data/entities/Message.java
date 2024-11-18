package com.chat.data.entities;


import com.chat.elasticsearch.listeners.MessageDocumentEntityListener;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "messages", indexes = {
        @Index(name = "chat_id_and_message_number_index", columnList = "chat_id, number", unique = true)
})
@EntityListeners(MessageDocumentEntityListener.class)
public class Message extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer number;

    @Column(name = "chat_id", nullable = false)
    private Long chatId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Chat chat;

}
