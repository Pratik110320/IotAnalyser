package com.pratik.IotAnalyser.controller;

import com.pratik.IotAnalyser.dtos.sensorDto.SensorRegistrationDto;
import com.pratik.IotAnalyser.dtos.sensorDto.SensorResponseDto;
import com.pratik.IotAnalyser.dtos.sensorDto.SensorSummaryDto;
import com.pratik.IotAnalyser.dtos.sensorDto.SensorUpdateDto;
import com.pratik.IotAnalyser.service.SensorDataService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensor")
@CrossOrigin(origins = "*")
public class SensorDataController {

    private final SensorDataService sensorDataService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    public SensorDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<SensorResponseDto> addSensorData(
            @RequestBody @Valid SensorRegistrationDto sensorRegistrationDto) {

        SensorResponseDto responseDto = sensorDataService.addSensorData(sensorRegistrationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<SensorSummaryDto>> getSensorData(){
        List<SensorSummaryDto> sensorSummaryDtoList = sensorDataService.getSensorData();
        return ResponseEntity.ok(sensorSummaryDtoList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<SensorResponseDto> getSensorDataDetails(@PathVariable Long id){
        SensorResponseDto sensorResponseDto = sensorDataService.getSensorDataDetails(id);
        return ResponseEntity.ok(sensorResponseDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSensorData(@PathVariable Long id, @RequestBody SensorUpdateDto sensorUpdateDto){
        sensorDataService.updateSensorData(id,sensorUpdateDto);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/type/{sensorType}")
    public ResponseEntity<List<SensorSummaryDto>> getSensorDataByTypes(@PathVariable String sensorType){
        List<SensorSummaryDto> sensorSummaryDto = sensorDataService.getSensorDataByTypes(sensorType);
        return ResponseEntity.ok().body(sensorSummaryDto);
    }

    @GetMapping("/anomalies")
    public ResponseEntity<List<SensorResponseDto>> getSensorDataByAnomalies(@PathVariable Boolean anomaly){
        List<SensorResponseDto> sensorResponseDto = sensorDataService.getSensorDataByAnomalies(anomaly);
        return ResponseEntity.ok().body(sensorResponseDto);
    }
}
