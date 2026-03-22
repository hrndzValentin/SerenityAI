package com.meditate.app_meditation.common.util;

import org.springframework.security.access.AccessDeniedException;
import java.util.Objects;
import java.util.UUID;

public class SecurityUtils {
    public static void assertOwnership(UUID ownerId, UUID authenticatedUserId) {
        if (!Objects.equals(ownerId, authenticatedUserId)) {
            throw new AccessDeniedException("You do not have permission to access this resource.");
        }
    }
}
