package com.iot.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "devices")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Device {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "device_id", unique = true, nullable = false) private String deviceId;
    private String name;
    @Builder.Default private String status = "OFF";
    @Builder.Default private boolean online = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id") private User user;
    @Column(name = "created_at") private LocalDateTime createdAt;
    @Column(name = "last_seen") private LocalDateTime lastSeen;
    @PrePersist public void prePersist() { createdAt = LocalDateTime.now(); }
}
