package com.smartmelon.backend.config;

import com.smartmelon.backend.user.Role;
import com.smartmelon.backend.user.User;
import com.smartmelon.backend.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Creates the first operator account so a fresh deployment can be signed in to.
 *
 * <p>It runs only when the user table is empty, so it can never overwrite a password that an
 * operator has since changed, and it refuses to run without a password from the environment rather
 * than falling back to a default one.
 */
@Component
@ConditionalOnProperty(prefix = "app.bootstrap", name = "enabled", havingValue = "true")
public class OperatorBootstrap implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(OperatorBootstrap.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BootstrapProperties properties;

    public OperatorBootstrap(
            UserRepository userRepository, PasswordEncoder passwordEncoder, BootstrapProperties properties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.properties = properties;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            log.debug("Accounts already exist; skipping operator bootstrap");
            return;
        }
        String username = properties.operatorUsername();
        String password = properties.operatorPassword();
        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            log.error(
                    "No account exists and bootstrap credentials are not configured. "
                            + "Set BOOTSTRAP_OPERATOR_USERNAME and BOOTSTRAP_OPERATOR_PASSWORD, then restart.");
            return;
        }

        User user = new User(
                username, passwordEncoder.encode(password), properties.operatorFullName(), Role.OPERATOR);
        userRepository.save(user);
        // The username is safe to log; the password is not, and never appears in any log statement.
        log.warn("Created the initial OPERATOR account '{}'. Change its password after the first sign-in.", username);
    }
}
