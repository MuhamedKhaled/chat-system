package com.chat.data.repositories;

import com.chat.data.entities.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findByApplicationIdAndNumber(Long applicationId, Integer number);
    Page<Chat> findByApplicationId(Long applicationId, Pageable pageable);

    @Query("SELECT MAX(c.number) FROM Chat c WHERE c.application.id = :applicationId")
    Optional<Integer> findMaxNumberByApplicationId(@Param("applicationId") Long applicationId);

    @Query("SELECT c.id FROM Chat c WHERE c.applicationId = :applicationId AND c.number = :chatNumber")
    Optional<Long> findChatIdByApplicationIdAndChatNumber(@Param("applicationId") Long applicationId, @Param("chatNumber") Integer chatNumber);
}