package com.smartmelon.backend.mqtt.handler;

import com.smartmelon.backend.alert.DeviceAlertService;
import com.smartmelon.backend.common.util.JsonSupport;
import com.smartmelon.backend.mqtt.MqttMessageHandler;
import com.smartmelon.backend.mqtt.TopicKind;
import com.smartmelon.backend.mqtt.payload.DeviceAlertPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** Stores an alert the edge agent raised itself. */
@Component
public class DeviceAlertMqttHandler implements MqttMessageHandler {

    private static final Logger log = LoggerFactory.getLogger(DeviceAlertMqttHandler.class);

    private final DeviceAlertService deviceAlertService;
    private final JsonSupport jsonSupport;

    public DeviceAlertMqttHandler(DeviceAlertService deviceAlertService, JsonSupport jsonSupport) {
        this.deviceAlertService = deviceAlertService;
        this.jsonSupport = jsonSupport;
    }

    @Override
    public TopicKind handles() {
        return TopicKind.ALERT;
    }

    @Override
    public void handle(String deviceCode, String payload) {
        DeviceAlertPayload alert;
        try {
            alert = jsonSupport.fromJson(payload, DeviceAlertPayload.class);
        } catch (RuntimeException ex) {
            log.warn("Discarding malformed alert from {}: {}", deviceCode, ex.getMessage());
            return;
        }
        deviceAlertService.record(deviceCode, alert);
    }
}
