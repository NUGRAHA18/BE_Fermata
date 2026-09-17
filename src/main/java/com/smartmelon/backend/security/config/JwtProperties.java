package com.smartmelon.backend.security.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * JWT signing settings.
 *
 * <p>The secret has no default and must be supplied through the environment (JWT_SECRET). Startup
 * fails rather than falling back to a built-in value, because a shipped default secret is the same
 * as no authentication at all. HS256 requires at least 256 bits of key material.
 */
@Validated
@ConfigurationProperties(prefix = "app.security.jwt")
public record JwtProperties(
        @NotBlank @Size(min = 32, message = "JWT secret must be at least 32 characters") String secret,
        @NotBlank String issuer,
        @NotNull Duration accessTokenTtl) {}
