package com.smartmelon.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * First-run account creation.
 *
 * <p>Deliberately not a database migration: a migration would commit a password to version control.
 * The credentials come from the environment, are used once when the user table is empty, and are
 * never logged.
 *
 * @param enabled whether to create the first operator when no account exists
 */
@ConfigurationProperties(prefix = "app.bootstrap")
public record BootstrapProperties(boolean enabled, String operatorUsername, String operatorPassword,
        String operatorFullName) {

    @Override
    public String toString() {
        return "BootstrapProperties{enabled=%s, operatorUsername=%s}".formatted(enabled, operatorUsername);
    }
}
