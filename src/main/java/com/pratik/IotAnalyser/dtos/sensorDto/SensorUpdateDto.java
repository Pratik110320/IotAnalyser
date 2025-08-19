package com.pratik.IotAnalyser.dtos.sensorDto;

public class SensorUpdateDto {
    private Double value;
    private String unit;

    public SensorUpdateDto() {
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
