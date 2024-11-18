package com.chat.data.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
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

    @Column(name = "messages_count", columnDefinition = "INT DEFAULT 0")
    private Integer messagesCount;

    @Column(name = "application_id", nullable = false)
    private Long applicationId; // Field to store the foreign key

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Application application;
}
