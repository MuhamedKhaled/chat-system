package com.chat.data.repositories;


import com.chat.data.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Application findByToken(byte[] token); // To retrieve applications by token
}