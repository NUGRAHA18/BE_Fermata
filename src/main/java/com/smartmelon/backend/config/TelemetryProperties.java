package com.smartmelon.backend.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Telemetry ingestion policy.
 *
 * @param autoRegisterSensors when true, a metric arriving for an unknown sensor creates a disabled
 *     placeholder sensor row instead of being dropped. This keeps data from being lost while the
 *     hardware team is still changing the sensor set; an operator can then name and enable it.
 * @param maxClockSkew how far into the future a device-supplied timestamp may be before it is
 *     replaced by the server receive time. Protects against edge devices with an unsynced clock.
 * @param maxMetricsPerMessage guard against a malformed or hostile payload exploding into rows.
 */
@ConfigurationProperties(prefix = "app.telemetry")
public record TelemetryProperties(
        boolean autoRegisterSensors, Duration maxClockSkew, int maxMetricsPerMessage) {}
