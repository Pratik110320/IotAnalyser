package com.pratik.IotAnalyser.repository;

import com.pratik.IotAnalyser.model.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorRepository extends JpaRepository<SensorData,Long> {

    @Query("SELECT s FROM SensorData s WHERE s.sensorType = ?1")
    List<SensorData> findSensorDataByType(String type);

    List<SensorData> findSensorDataByAnomaly(Boolean anomaly);
}
