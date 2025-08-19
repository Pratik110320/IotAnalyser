package com.pratik.IotAnalyser.repository;

import com.pratik.IotAnalyser.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device,Long> {

    List<Device> findDeviceByStatus(Device.Status status);
}
