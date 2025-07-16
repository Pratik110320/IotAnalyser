package com.pratik.IotAnalyser.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

import java.time.LocalDateTime;

@Entity
@Data
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deviceId;
    private String deviceName;
    private String deviceType;
    private LocalDateTime registeredAt;
    private LocalDateTime lastActiveAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status{
        ONLINE, OFFLINE, DISCONNECTE
    }
}
