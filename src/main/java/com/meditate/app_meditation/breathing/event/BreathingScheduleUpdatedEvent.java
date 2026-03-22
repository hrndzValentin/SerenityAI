package com.meditate.app_meditation.breathing.event;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public record BreathingScheduleUpdatedEvent(
    UUID userId,
    List<LocalTime> scheduledTimes
) {}
