package com.pratik.IotAnalyser.webSocket;



import com.pratik.IotAnalyser.dtos.sensorDto.SensorRegistrationDto;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class SensorDataWebSocketController {

    @MessageMapping("/sensorData")
    @SendTo("/topic/sensorData")
    public SensorRegistrationDto sendSensorData(SensorRegistrationDto data) {
        System.out.println("Received: " + data);
        return data; // Broadcast to subscribers
    }
}
