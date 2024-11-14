package com.chat.data.entities;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "chats", indexes = {
        @Index(name = "application_id_and_chat_number_index", columnList = "application_id, number", unique = true)
})
public class Chat extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer number;

    @Column(name = "application_id", nullable = false)
    private Long applicationId;

    @Column(name = "messages_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer messagesCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Application application;
}
