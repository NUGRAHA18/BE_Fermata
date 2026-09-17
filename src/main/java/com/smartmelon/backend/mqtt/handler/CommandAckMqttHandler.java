package com.smartmelon.backend.mqtt.handler;

import com.smartmelon.backend.actuator.ActuatorCommandService;
import com.smartmelon.backend.common.util.JsonSupport;
import com.smartmelon.backend.mqtt.MqttMessageHandler;
import com.smartmelon.backend.mqtt.TopicKind;
import com.smartmelon.backend.mqtt.payload.CommandAckPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/** Applies a device acknowledgement to the matching command record. */
@Component
public class CommandAckMqttHandler implements MqttMessageHandler {

    private static final Logger log = LoggerFactory.getLogger(CommandAckMqttHandler.class);

    private final ActuatorCommandService commandService;
    private final JsonSupport jsonSupport;

    public CommandAckMqttHandler(ActuatorCommandService commandService, JsonSupport jsonSupport) {
        this.commandService = commandService;
        this.jsonSupport = jsonSupport;
    }

    @Override
    public TopicKind handles() {
        return TopicKind.COMMAND_ACK;
    }

    @Override
    public void handle(String deviceCode, String payload) {
        CommandAckPayload ack;
        try {
            ack = jsonSupport.fromJson(payload, CommandAckPayload.class);
        } catch (RuntimeException ex) {
            log.warn("Discarding malformed command acknowledgement from {}: {}", deviceCode, ex.getMessage());
            return;
        }
        commandService.applyAcknowledgement(deviceCode, ack);
    }
}
