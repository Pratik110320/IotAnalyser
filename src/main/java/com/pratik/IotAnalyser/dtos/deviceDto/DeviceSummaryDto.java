package com.pratik.IotAnalyser.dtos.deviceDto;

import com.pratik.IotAnalyser.model.Device;
import lombok.Data;

@Data
public class DeviceSummaryDto {
    private String deviceName;
    private String deviceType;
    private Device.Status status;

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

    public Device.Status getStatus() {
        return status;
    }

    public void setStatus(Device.Status status) {
        this.status = status;
    }
}
