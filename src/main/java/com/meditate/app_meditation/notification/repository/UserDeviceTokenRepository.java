package com.meditate.app_meditation.notification.repository;

import com.meditate.app_meditation.notification.entity.UserDeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface UserDeviceTokenRepository extends JpaRepository<UserDeviceToken, UUID> {
    List<UserDeviceToken> findByUserId(UUID userId);
}
