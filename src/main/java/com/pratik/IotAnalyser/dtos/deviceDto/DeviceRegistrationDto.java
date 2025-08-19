package com.pratik.IotAnalyser.dtos.deviceDto;

import lombok.Data;

public class DeviceRegistrationDto {

    private String deviceName;
    private String deviceType;

    public DeviceRegistrationDto(String deviceName, String deviceType) {
        this.deviceName = deviceName;
        this.deviceType = deviceType;
    }

    public DeviceRegistrationDto() {
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
