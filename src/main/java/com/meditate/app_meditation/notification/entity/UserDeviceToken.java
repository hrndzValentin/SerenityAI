package com.meditate.app_meditation.notification.entity;

import com.meditate.app_meditation.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_device_tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDeviceToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "fcm_token", nullable = false)
    private String fcmToken;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;
}
