package com.smartmelon.backend.alert;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface AlertRepository extends JpaRepository<Alert, Long>, JpaSpecificationExecutor<Alert> {

    Page<Alert> findByAcknowledgedOrderByCreatedAtDesc(boolean acknowledged, Pageable pageable);

    Page<Alert> findByAcknowledgedAndSeverityOrderByCreatedAtDesc(
            boolean acknowledged, AlertSeverity severity, Pageable pageable);

    Page<Alert> findBySeverityOrderByCreatedAtDesc(AlertSeverity severity, Pageable pageable);

    long countByAcknowledgedFalse();

    long countByAcknowledgedFalseAndSeverity(AlertSeverity severity);

    /** Used to avoid raising the same infrastructure alert again while it is still unacknowledged. */
    boolean existsByTypeAndRelatedDeviceIdAndAcknowledgedFalse(String type, Long deviceId);

    @Query("select a from Alert a where a.acknowledged = false order by a.severity desc, a.createdAt desc")
    List<Alert> findActive(Pageable pageable);
}
