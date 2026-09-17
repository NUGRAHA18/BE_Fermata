package com.smartmelon.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Entry point of the Smart Melon backend.
 *
 * <p>The backend is the application/business layer that sits between the PWA frontend and the IoT
 * infrastructure (MQTT broker / edge device). The frontend never talks to MQTT directly and never
 * sees hardware details such as GPIO pins or sensor protocols.
 */
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
public class SmartMelonBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartMelonBackendApplication.class, args);
    }
}
