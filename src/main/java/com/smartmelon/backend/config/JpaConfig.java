package com.smartmelon.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/** Enables the created/updated timestamp auditing used by the entity base classes. */
@Configuration
@EnableJpaAuditing
public class JpaConfig {}
