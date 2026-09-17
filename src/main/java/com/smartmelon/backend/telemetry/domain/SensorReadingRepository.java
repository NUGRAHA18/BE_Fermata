package com.smartmelon.backend.telemetry.domain;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {

    Optional<SensorReading> findFirstBySensorIdOrderByRecordedAtDescIdDesc(Long sensorId);

    Page<SensorReading> findBySensorIdAndRecordedAtBetweenOrderByRecordedAtDesc(
            Long sensorId, Instant from, Instant to, Pageable pageable);

    /**
     * Latest reading of every enabled sensor, used by the dashboard.
     *
     * <p>Written as a subquery rather than N queries: the dashboard must stay a single round-trip
     * regardless of how many sensors the hardware team ends up installing. Disabled sensors are
     * excluded so that an auto-registered, not-yet-reviewed metric does not surface on the dashboard
     * under a placeholder name - its readings are still stored.
     */
    @Query(
            """
            select r from SensorReading r
              join fetch r.sensor s
              join fetch r.device d
            where s.enabled = true
              and r.id in (
                select max(r2.id) from SensorReading r2 group by r2.sensor.id
              )
            order by s.code asc
            """)
    List<SensorReading> findLatestPerSensor();

    long countByReceivedAtAfter(Instant since);
}
