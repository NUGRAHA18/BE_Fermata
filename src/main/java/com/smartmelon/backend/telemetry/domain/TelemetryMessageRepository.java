package com.smartmelon.backend.telemetry.domain;

import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelemetryMessageRepository extends JpaRepository<TelemetryMessage, Long> {

    Page<TelemetryMessage> findByDeviceIdOrderByReceivedAtDesc(Long deviceId, Pageable pageable);

    long countByStatusAndReceivedAtAfter(TelemetryStatus status, Instant since);
}
