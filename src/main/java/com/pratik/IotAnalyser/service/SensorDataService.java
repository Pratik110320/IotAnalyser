package com.pratik.IotAnalyser.service;

import com.pratik.IotAnalyser.dtos.sensorDto.*;
import com.pratik.IotAnalyser.model.SensorData;
import com.pratik.IotAnalyser.exception.ResourceNotFoundException;
import com.pratik.IotAnalyser.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SensorDataService {

    private final SensorRepository sensorRepository;
    private final SensorMapper sensorMapper;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    public SensorDataService(SensorRepository sensorRepository,SensorMapper sensorMapper) {
        this.sensorRepository = sensorRepository;
        this.sensorMapper = sensorMapper;
    }



    @Transactional
    public SensorResponseDto addSensorData(SensorRegistrationDto sensorRegistrationDto) {
        if (sensorRegistrationDto == null) {
            throw new IllegalArgumentException("Sensor Registration Data can't be null.");
        }

        SensorData sensorData = sensorMapper.toEntity(sensorRegistrationDto);
        sensorData.setTimestamp(LocalDateTime.now());

        SensorData savedSensor = sensorRepository.save(sensorData);

        // Broadcast saved data to WebSocket subscribers
        SensorResponseDto broadcastDto = sensorMapper.toResponseDto(savedSensor);
        messagingTemplate.convertAndSend("/topic/sensorData", broadcastDto);

        return broadcastDto;
    }

    public List<SensorSummaryDto> getSensorData() {
    List<SensorData> sensorData = sensorRepository.findAll();
    if(sensorData.isEmpty()){
        throw new ResourceNotFoundException("No Sensor Data Found.");
    }
    return sensorData.stream()
            .map(sensorMapper::toSummaryDto)
            .collect(Collectors.toList());
    }



    public SensorResponseDto getSensorDataDetails(Long id) {
        SensorData sensorData = sensorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No Sensor Data Found."));
        return sensorMapper.toResponseDto(sensorData);
    }


    @Transactional
    public SensorResponseDto updateSensorData(Long id, SensorUpdateDto sensorUpdateDto) {
        SensorData existingData = sensorRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No Sensor Data Found."));
        sensorMapper.updateSensorDataFromDto(sensorUpdateDto,existingData);
        SensorData savedData = sensorRepository.save(existingData);
        if (savedData == null){
            throw new IllegalArgumentException("Sensor Data Could Not Be Updated.");
        }
        return sensorMapper.toResponseDto(savedData);
    }


    public List<SensorSummaryDto> getSensorDataByTypes(String sensorType) {
        List<SensorData> sensorData = sensorRepository.findSensorDataByType(sensorType);
        if(sensorData.isEmpty()){
            throw new ResourceNotFoundException("No Sensor Data Found.");
        }

        return  sensorData.stream()
                .map(sensorMapper::toSummaryDto)
                .collect(Collectors.toList());
    }

    public List<SensorResponseDto> getSensorDataByAnomalies(Boolean anomaly) {

        if (!Boolean.TRUE.equals(anomaly)) {
            throw new IllegalArgumentException("This endpoint only supports anomaly = true");
        }
        List<SensorData> sensorData = sensorRepository.findSensorDataByAnomaly(anomaly);
        if (sensorData.isEmpty()) {
            throw new ResourceNotFoundException("No sensor data found with anomaly = true");
        }
        return sensorData.stream()
                .map(sensorMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
