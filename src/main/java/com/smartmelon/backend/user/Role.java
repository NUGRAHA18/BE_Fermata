package com.smartmelon.backend.user;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * Roles an account can hold.
 *
 * <p>Phase 1 ships a single role on purpose. The extension point is deliberate and small: add a
 * constant here and reference it from endpoint security. No permission table and no role hierarchy -
 * those are only worth building once the team knows it needs them.
 */
public enum Role {
    OPERATOR;

    /** Role names as used in security expressions, kept in one place to avoid typo-driven bugs. */
    public static final class Names {
        public static final String OPERATOR = "OPERATOR";

        private Names() {}
    }

    /** Spring Security expects role authorities to carry the {@code ROLE_} prefix. */
    public String authority() {
        return "ROLE_" + name();
    }

    public List<GrantedAuthority> grantedAuthorities() {
        return List.of(new SimpleGrantedAuthority(authority()));
    }
}
