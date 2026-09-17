package com.smartmelon.backend.dashboard;

import com.smartmelon.backend.actuator.dto.ActuatorStatusResponse;
import com.smartmelon.backend.ai.AiDetectionResponse;
import com.smartmelon.backend.alert.AlertResponse;
import com.smartmelon.backend.device.DeviceStatusResponse;
import com.smartmelon.backend.telemetry.dto.SensorReadingResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;

/**
 * Everything the main dashboard needs, in one response.
 *
 * <p>The shape exists so the PWA can render its first screen with a single request instead of a
 * dozen, and so that adding a panel later is a change here rather than another round-trip.
 */
@Schema(name = "DashboardOverview")
public record DashboardOverviewResponse(
        SystemSection system,
        DeviceSection devices,
        SensorSection sensors,
        List<ActuatorStatusResponse> actuators,
        AlertSection alerts,
        @Schema(description = "Null until the vision model reports something") AiDetectionResponse latestAiDetection,
        List<ActivityEntry> recentActivity) {

    /**
     * How the backend itself is running.
     *
     * @param mode DEVELOPMENT means mock messaging and the simulator may be active
     * @param messagingTransport which transport is wired in, so a developer can tell mock from real
     */
    @Schema(name = "DashboardSystemSection")
    public record SystemSection(
            String mode, String messagingTransport, boolean messagingConnected, Instant generatedAt) {}

    @Schema(name = "DashboardDeviceSection")
    public record DeviceSection(long total, long online, long offline, long unknown, List<DeviceStatusResponse> items) {}

    @Schema(name = "DashboardSensorSection")
    public record SensorSection(
            long total,
            long enabled,
            @Schema(description = "Latest value of every enabled sensor") List<SensorReadingResponse> latestReadings) {}

    @Schema(name = "DashboardAlertSection")
    public record AlertSection(long unacknowledged, long critical, long warning, List<AlertResponse> active) {}

    /** One line in the activity feed. */
    @Schema(name = "DashboardActivityEntry")
    public record ActivityEntry(
            @Schema(example = "ACTUATOR_COMMAND") String kind, Instant at, String summary, String detail) {}
}
