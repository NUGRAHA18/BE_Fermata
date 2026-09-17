package com.smartmelon.backend.alert;

import com.smartmelon.backend.common.response.PageResponse;
import com.smartmelon.backend.security.auth.CurrentUserProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/alerts")
@Tag(name = "Alerts", description = "Operational alerts and acknowledgement")
@SecurityRequirement(name = "bearerAuth")
@Validated
public class AlertController {

    private final AlertService alertService;
    private final CurrentUserProvider currentUserProvider;

    public AlertController(AlertService alertService, CurrentUserProvider currentUserProvider) {
        this.alertService = alertService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping
    @Operation(summary = "List alerts", description = "Optionally filtered by acknowledgement state and severity.")
    public PageResponse<AlertResponse> list(
            @RequestParam(required = false) Boolean acknowledged,
            @RequestParam(required = false) AlertSeverity severity,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(200) int size) {
        return alertService.list(acknowledged, severity, PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get one alert")
    public AlertResponse get(@PathVariable Long id) {
        return alertService.get(id);
    }

    @PatchMapping("/{id}/acknowledge")
    @Operation(
            summary = "Acknowledge an alert",
            description = "Records the signed-in operator as the one who acknowledged it.")
    public AlertResponse acknowledge(@PathVariable Long id) {
        return alertService.acknowledge(id, currentUserProvider.requireUsername());
    }
}
