package com.pratik.IotAnalyser.dtos.sensorDto;

import com.pratik.IotAnalyser.model.SensorData;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SensorMapper {

    // Entity to Response DTO (Fixed to return SensorResponseDto)
    SensorResponseDto toResponseDto(SensorData sensorData);

    // Request DTO to Entity
    SensorData toEntity(SensorRegistrationDto requestDto);

    // Entity to Summary DTO
    SensorSummaryDto toSummaryDto(SensorData sensorData);

    // Entity to Anomaly DTO
    SensorAnomalyDto toAnomalyDto(SensorData sensorData);

    // Update DTO to existing Entity (partial update)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSensorDataFromDto(SensorUpdateDto updateDto, @MappingTarget SensorData sensorData);

    // Bulk conversions
    List<SensorResponseDto> toResponseDtoList(List<SensorData> entities); // Updated to SensorResponseDto
    List<SensorSummaryDto> toSummaryDtoList(List<SensorData> entities);
    List<SensorAnomalyDto> toAnomalyDtoList(List<SensorData> entities);
}
