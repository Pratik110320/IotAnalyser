package com.pratik.IotAnalyser.dtos.deviceDto;

import com.pratik.IotAnalyser.model.Device;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    Device deviceToEntity(DeviceRegistrationDto deviceRegistrationDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateDeviceFromDto(DeviceUpdateDto dto, @MappingTarget Device entity);

    DeviceResponseDto toDeviceResponseDto(Device device);
    DeviceSummaryDto toDeviceSummaryDto(Device device);

    List<DeviceSummaryDto> toSummaryDtoList(List<Device> devices);
}
