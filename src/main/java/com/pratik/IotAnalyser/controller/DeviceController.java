package com.pratik.IotAnalyser.controller;

import com.pratik.IotAnalyser.dtos.deviceDto.DeviceRegistrationDto;
import com.pratik.IotAnalyser.dtos.deviceDto.DeviceResponseDto;
import com.pratik.IotAnalyser.dtos.deviceDto.DeviceSummaryDto;
import com.pratik.IotAnalyser.dtos.deviceDto.DeviceUpdateDto;
import com.pratik.IotAnalyser.model.Device;
import com.pratik.IotAnalyser.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/device")
@CrossOrigin(origins = "*")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public ResponseEntity<DeviceResponseDto> addDevice(@RequestBody @Valid DeviceRegistrationDto deviceRegistrationDto){
        DeviceResponseDto device1 = deviceService.addDevice(deviceRegistrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(device1);
    }



    @GetMapping
    public ResponseEntity<List<DeviceSummaryDto>> getAllDevices(){
        List<DeviceSummaryDto> devicesList = deviceService.getAllDevices();
        return ResponseEntity.ok(devicesList);
    }



    @GetMapping("/detail")
    public ResponseEntity<List<DeviceResponseDto>> getAllDevicesWithDetails(){
        List<DeviceResponseDto> devicesList = deviceService.getAllDevicesWithDetails();
        return ResponseEntity.ok(devicesList);
    }



    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponseDto> getDevice(@PathVariable("id") Long deviceId){
        DeviceResponseDto deviceById = deviceService.getDevice(deviceId);
        return ResponseEntity.ok(deviceById);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDevice(@PathVariable("id")  Long deviceId, @RequestBody DeviceUpdateDto deviceUpdateDto){
         deviceService.updateDevice(deviceId,deviceUpdateDto);
        return ResponseEntity.ok().build();
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable("id") Long deviceId){
        deviceService.deleteDevice(deviceId);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/status/{status}")
    public ResponseEntity<List<DeviceSummaryDto>> getDeviceByStatus(@PathVariable String status) {
        try {
            Device.Status deviceStatus = Device.Status.valueOf(status.toUpperCase());
            List<DeviceSummaryDto> devices = deviceService.getDeviceByStatus(deviceStatus);
            return ResponseEntity.ok(devices);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid status: " + status + ". Valid values: " + Arrays.toString(Device.Status.values()));
        }
    }
}
