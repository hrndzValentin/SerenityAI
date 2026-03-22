package com.meditate.app_meditation.notification.entity;

import com.meditate.app_meditation.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "notification_schedules", indexes = {
    @Index(name = "idx_notif_user_time", columnList = "user_id, scheduled_time")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type")
    private EntityType entityType;

    @Column(name = "entity_id")
    private Long entityId;
    
    @Column(name = "scheduled_time")
    private LocalTime scheduledTime;
    
    @Column(name = "days_of_week")
    private Integer daysOfWeek; // Bitmask (e.g., 127 for daily)
    
    @Column(name = "is_active")
    private boolean isActive;
    
    private String timezone;

    public enum EntityType {
        MEDITATION, BREATHING
    }
}
