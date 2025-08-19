package com.pratik.IotAnalyser.service;

import com.pratik.IotAnalyser.dtos.deviceDto.*;

import com.pratik.IotAnalyser.exception.ResourceNotFoundException;
import com.pratik.IotAnalyser.model.Device;
import com.pratik.IotAnalyser.repository.DeviceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    private final DeviceMapper deviceMapper;

    public DeviceService(DeviceRepository deviceRepository,DeviceMapper deviceMapper) {
        this.deviceRepository = deviceRepository;
        this.deviceMapper = deviceMapper;
    }




    @Transactional
    public DeviceResponseDto addDevice(DeviceRegistrationDto deviceRegistrationDto) {
        Device device = deviceMapper.deviceToEntity(deviceRegistrationDto);
        device.setStatus(Device.Status.ONLINE);
        device.setRegisteredAt(LocalDateTime.now());
        deviceRepository.save(device);
        return deviceMapper.toDeviceResponseDto(device);
    }



    public List<DeviceSummaryDto> getAllDevices() {
        List<Device> device = deviceRepository.findAll();
        if (device.isEmpty()){
            throw new ResourceNotFoundException("No devices Found.");
        }
        return device.stream()
                .map(deviceMapper::toDeviceSummaryDto)
                .collect(Collectors.toList());
    }



    public DeviceResponseDto getDevice(Long deviceId) {
        Device deviceById = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device Not Found."));
        return deviceMapper.toDeviceResponseDto(deviceById);

    }



    @Transactional
    public DeviceResponseDto updateDevice(Long deviceId, DeviceUpdateDto deviceUpdateDto) {
        Device existingDevice = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Device Not Found."));

        deviceMapper.updateDeviceFromDto(deviceUpdateDto, existingDevice);
        Device savedDevice = deviceRepository.save(existingDevice);
        return deviceMapper.toDeviceResponseDto(savedDevice);
    }



    @Transactional
    public void deleteDevice(Long deviceId) {
    Device device = deviceRepository.findById(deviceId)
            .orElseThrow(() -> new ResourceNotFoundException("Device Not Found."));

    deviceRepository.delete(device);
    }



    public List<DeviceSummaryDto> getDeviceByStatus(Device.Status status) {
        List<Device> device = deviceRepository.findDeviceByStatus(status);

        if (device.isEmpty()){
            throw new ResourceNotFoundException("Device Not Found." + status);
        }
        return device.stream()
                .map(deviceMapper::toDeviceSummaryDto)
                .collect(Collectors.toList());
    }




    public List<DeviceResponseDto> getAllDevicesWithDetails() {
        List<Device> device = deviceRepository.findAll();
        if (device.isEmpty()){
            throw new ResourceNotFoundException("No devices Found.");
        }
        return device.stream()
                .map(deviceMapper::toDeviceResponseDto)
                .collect(Collectors.toList());
    }
}
