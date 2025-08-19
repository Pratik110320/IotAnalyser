package com.pratik.IotAnalyser.dtos.sensorDto;

import java.time.LocalDateTime;

public class SensorAnomalyDto {
    private Long id;
    private String sensorType;
    private Double value;
    private LocalDateTime timestamp;
    private Boolean anomaly;

    public SensorAnomalyDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getAnomaly() {
        return anomaly;
    }

    public void setAnomaly(Boolean anomaly) {
        this.anomaly = anomaly;
    }
}
