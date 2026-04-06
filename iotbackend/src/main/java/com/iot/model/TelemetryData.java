package com.iot.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "telemetry")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TelemetryData {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "device_id", nullable = false) private String deviceId;
    private Double voltage;
    private Double current;
    private Double power;
    private Double energy;
    private Double frequency;
    @Column(name = "power_factor") private Double powerFactor;
    private String status;
    @Column(nullable = false) private LocalDateTime timestamp;
    @PrePersist public void prePersist() { if (timestamp == null) timestamp = LocalDateTime.now(); }
}
