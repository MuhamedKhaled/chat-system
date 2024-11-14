package com.chat.data.repositories;

import com.chat.data.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChatIdOrderByNumber(Long chatId); // To retrieve messages in a specific chat
}