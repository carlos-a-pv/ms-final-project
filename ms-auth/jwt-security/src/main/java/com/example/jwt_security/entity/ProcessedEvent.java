package com.example.jwt_security.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "processed_events",
        uniqueConstraints = @UniqueConstraint(columnNames = "eventId"))
public class ProcessedEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String eventId;
    private Instant timestamp;
    private String consumer;

}
