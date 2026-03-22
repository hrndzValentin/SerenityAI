package com.meditate.app_meditation.meditation.entity;

import com.meditate.app_meditation.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "meditation_routines", indexes = {
    @Index(name = "idx_routine_user_id", columnList = "user_id"),
    @Index(name = "idx_routine_focus_area", columnList = "focus_area")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MeditationRoutine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "duration_minutes")
    private Integer durationMinutes;
    
    @Column(name = "ai_generated")
    private boolean aiGenerated;
    
    @Column(name = "is_public")
    private boolean isPublic;

    @Enumerated(EnumType.STRING)
    @Column(name = "focus_area")
    private FocusArea focusArea;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<String> techniques;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum FocusArea {
        ANXIETY, SLEEP, FOCUS, STRESS
    }
}
