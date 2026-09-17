package com.smartmelon.backend.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Top-level application settings.
 *
 * <p>{@link Mode#DEVELOPMENT} is what allows the backend to run without any hardware attached: mock
 * messaging and the telemetry simulator are only wired in that mode. Nothing in the business layer
 * branches on this value - the mode selects which infrastructure beans are created.
 */
@Validated
@ConfigurationProperties(prefix = "app")
public record AppProperties(@NotNull Mode mode) {

    public enum Mode {
        DEVELOPMENT,
        PRODUCTION
    }

    public boolean isDevelopment() {
        return mode == Mode.DEVELOPMENT;
    }
}
