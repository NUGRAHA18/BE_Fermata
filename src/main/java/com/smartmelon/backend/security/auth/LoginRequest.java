package com.smartmelon.backend.security.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Credentials supplied by the operator console. Never logged, never echoed back. */
@Schema(name = "LoginRequest")
public record LoginRequest(
        @NotBlank @Size(max = 64) @Schema(example = "operator") String username,
        @NotBlank @Size(max = 128) @Schema(example = "change-me") String password) {

    /** Guards against the password leaking into a debug log through an accidental toString(). */
    @Override
    public String toString() {
        return "LoginRequest{username=%s}".formatted(username);
    }
}
