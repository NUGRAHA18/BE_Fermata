package com.smartmelon.backend.security.config;

import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Cross-origin settings for the PWA.
 *
 * <p>Origins are always an explicit list. A wildcard is never used: the API is credentialed, and
 * the production frontend domain is a deployment detail that belongs in the environment.
 */
@ConfigurationProperties(prefix = "app.security.cors")
public record CorsProperties(
        List<String> allowedOrigins,
        List<String> allowedMethods,
        List<String> allowedHeaders,
        boolean allowCredentials,
        Duration maxAge) {}
