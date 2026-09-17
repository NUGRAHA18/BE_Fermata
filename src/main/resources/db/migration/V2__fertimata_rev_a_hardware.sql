-- FERTIMATA Rev A hardware alignment.
--
-- Every change below is additive and nullable, so existing rows stay valid and a different hardware
-- revision is still expressed as data rather than schema. See docs/hasil-penyesuaian-1.md.

-- ---------------------------------------------------------------------------
-- Site power, as reported by the edge device
-- ---------------------------------------------------------------------------
-- Rev A runs from a DC UPS with VRLA batteries. The edge agent decides whether the site is on mains
-- or on battery (from the PZEM-016 reading); the backend only records what it was told and uses it
-- as an interlock input. The vocabulary (MAINS, BATTERY, UNKNOWN, ...) is owned by the edge agent.
ALTER TABLE device
    ADD COLUMN power_source            VARCHAR(16),
    ADD COLUMN power_source_updated_at TIMESTAMPTZ;

COMMENT ON COLUMN device.power_source IS 'Last power source REPORTED by the device, e.g. MAINS or BATTERY. Null when the device never reports one.';

-- ---------------------------------------------------------------------------
-- Actuator run-time limit
-- ---------------------------------------------------------------------------
-- A software ceiling on how long one command may keep an output on. It complements, and never
-- replaces, the mechanical timer relay the hardware design requires in series with the pumps.
ALTER TABLE actuator
    ADD COLUMN max_run_seconds INTEGER;

COMMENT ON COLUMN actuator.max_run_seconds IS 'Upper bound for durationSeconds on an activating command. Null means no software limit.';

-- ---------------------------------------------------------------------------
-- AI detections: multi-label scores and camera stations
-- ---------------------------------------------------------------------------
-- Stage 2 of the Rev A pipeline is a multi-label classifier with independent sigmoids (N, P and K
-- stress at the same time), so one result carries several scores rather than a single label.
-- The trolley stops at numbered stations; the station count is not final, so it is free text.
ALTER TABLE ai_detection
    ALTER COLUMN label DROP NOT NULL,
    ADD COLUMN scores       JSONB,
    ADD COLUMN station_code VARCHAR(32),
    ADD COLUMN capture_id   VARCHAR(64);

COMMENT ON COLUMN ai_detection.scores IS 'Label -> score map from a multi-label model, stored as reported. Not interpreted by the backend.';
COMMENT ON COLUMN ai_detection.station_code IS 'Camera station the frame was taken at, e.g. ST-01. Free text: the station count is not final.';
COMMENT ON COLUMN ai_detection.capture_id IS 'Groups results produced from the same capture run or frame.';

CREATE INDEX idx_detection_station_detected ON ai_detection (station_code, detected_at DESC);
