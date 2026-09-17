package com.smartmelon.backend.config;

import java.util.List;
import java.util.Locale;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * How reported power sources are interpreted.
 *
 * <p>The vocabulary belongs to the edge agent, so the backend only needs to know which reported
 * values mean "normal supply". Anything else - {@code BATTERY}, {@code UNKNOWN}, a value this build
 * has never seen - is treated as running on backup, which is the safe reading for an interlock.
 *
 * @param normalSources power source values that mean the site is on its normal supply
 */
@ConfigurationProperties(prefix = "app.power")
public record PowerProperties(List<String> normalSources) {

    public PowerProperties {
        normalSources = normalSources == null || normalSources.isEmpty()
                ? List.of("MAINS")
                : normalSources.stream().map(PowerProperties::normalise).toList();
    }

    public boolean isNormal(String source) {
        return source != null && normalSources.contains(normalise(source));
    }

    public static String normalise(String source) {
        return source == null ? null : source.trim().toUpperCase(Locale.ROOT);
    }
}
