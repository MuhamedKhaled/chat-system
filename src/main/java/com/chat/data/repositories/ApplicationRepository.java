package com.chat.data.repositories;


import com.chat.data.entities.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("SELECT a FROM Application a WHERE a.token = :token")
    Optional<Application> findByToken(@Param("token") byte[] token);

    @Query(value = "SELECT * FROM applications ORDER BY id LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Application> findAllPaged(@Param("offset") int offset, @Param("limit") int limit);
}