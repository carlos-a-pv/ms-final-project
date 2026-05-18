package com.example.jwt_security.repository;

import com.example.jwt_security.entity.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent,Long> {
    boolean existsByEventId(String eventId);
}
