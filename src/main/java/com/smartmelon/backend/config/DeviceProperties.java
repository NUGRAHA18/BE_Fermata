package com.smartmelon.backend.config;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Device liveness and registration policy.
 *
 * @param offlineTimeout how long silence is tolerated before a device is considered offline.
 *     Timeout-based detection is used deliberately: a device that loses power never gets to announce
 *     that it went away. TODO(hardware): must be a small multiple of the heartbeat interval the edge
 *     device will actually use, which is not decided yet - the shipped default is provisional.
 * @param livenessCheckInterval how often the backend re-evaluates which devices have gone quiet
 * @param autoRegister when true, a message from an unknown device code creates a device row instead
 *     of being dropped. Convenient while hardware is being bench-tested; consider turning it off in
 *     production so that only provisioned devices can create records.
 */
@Validated
@ConfigurationProperties(prefix = "app.device")
public record DeviceProperties(
        @NotNull Duration offlineTimeout, @NotNull Duration livenessCheckInterval, boolean autoRegister) {}
