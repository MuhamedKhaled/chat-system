package com.chat.data.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "applications", indexes = {
        @Index(name = "application_token_index", columnList = "token", unique = true)
})
public class Application extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 16)
    private byte[] token;

    @Column(nullable = false)
    private String name;

    @Column(name = "chats_count", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer chatsCount = 0;

}
