package com.pratik.IotAnalyser.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
@Configuration
public class MessagingTemplateConfig {

    @Bean
    public MappingJackson2MessageConverter jackson2MessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(mapper);
        return converter;
    }

    @Bean
    public ApplicationListener<ContextRefreshedEvent> websocketConverterInitializer(
            SimpMessagingTemplate messagingTemplate,
            MappingJackson2MessageConverter converter
    ) {
        return event -> messagingTemplate.setMessageConverter(converter);
    }
}
