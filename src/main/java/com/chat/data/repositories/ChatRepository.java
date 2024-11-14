package com.chat.data.repositories;

import com.chat.data.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByApplicationIdAndNumber(Long applicationId, Integer number); // To find chat by app and chat number
}