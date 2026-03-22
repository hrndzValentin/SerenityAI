package com.meditate.app_meditation.breathing.entity;

import com.meditate.app_meditation.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "breathing_exercises", indexes = {
    @Index(name = "idx_breathing_user_id", columnList = "user_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreathingExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private BreathingPattern pattern;

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "scheduled_times", columnDefinition = "time[]")
    private List<LocalTime> scheduledTimes;

    @Column(name = "total_daily_reps")
    private int totalDailyReps;
    
    @Column(name = "is_active")
    private boolean isActive;

    public enum Category {
        CALMING, ENERGIZING, BALANCING
    }

    // Java 21 Record for JSONB mapping
    public record BreathingPattern(int inhale, int hold, int exhale, int postHold) {}
}
