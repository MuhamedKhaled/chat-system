package com.chat.data.repositories;

import com.chat.data.entities.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByChatId(Long chatId, Pageable pageable);
    @Query("SELECT MAX(m.number) FROM Message m WHERE m.chatId = :chatId")
    Optional<Integer> findMaxNumberByChatId(Long chatId);

    Long countByChatId(Long chatId);
}