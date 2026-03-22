# SerenityAI API Contract Document

**Version:** 1.0.0  
**Base URL:** `http://localhost:8080/api/v1`

---

## 🔐 Authentication Flow

SerenityAI uses **JWT (JSON Web Token)** for stateless authentication.

1.  **Obtain Token:** Register or Login via the `/auth` endpoints.
2.  **Use Token:** Include the token in the header of all protected requests:
    `Authorization: Bearer <your_jwt_token>`
3.  **Expiry:** Tokens are valid for 1 hour.

---

## 🛠 Common Enums

| Enum Name | Allowed Values |
| :--- | :--- |
| **FocusArea** | `ANXIETY`, `SLEEP`, `FOCUS`, `STRESS` |
| **BreathingCategory** | `CALMING`, `ENERGIZING`, `BALANCING` |
| **SubscriptionStatus** | `FREE`, `PREMIUM`, `ENTERPRISE` |
| **EntityType** | `MEDITATION`, `BREATHING` |

---

## 📦 Global Response Wrapper

All successful responses follow this JSON envelope:

| Field | Type | Description |
| :--- | :--- | :--- |
| `success` | Boolean | Always `true` for 2xx responses. |
| `message` | String | Description of the operation result. |
| `data` | Object/Array | The requested resource payload. |
| `timestamp` | String | ISO-8601 timestamp. |
| `pagination` | Object | (Optional) Contains `page`, `size`, `totalElements`, `totalPages`. |

---

## 1. Authentication Module (`/auth`)

### Register User
**POST** `/auth/register`  
*Description:* Creates a new user account and returns an access token.

**Request Body:**
| Field | Type | Required | Validation |
| :--- | :--- | :--- | :--- |
| `firstName` | String | Yes | Max 50 characters |
| `lastName` | String | Yes | Max 50 characters |
| `email` | String | Yes | Valid email format |
| `password` | String | Yes | Minimum 8 characters |

**Example Request:**
```json
{
  "firstName": "Jane",
  "lastName": "Doe",
  "email": "jane.doe@example.com",
  "password": "securePassword123"
}
```

---

### Login
**POST** `/auth/authenticate`  
*Description:* Validates credentials and returns a JWT.

**Request Body:**
| Field | Type | Required | Description |
| :--- | :--- | :--- | :--- |
| `email` | String | Yes | User's registered email |
| `password` | String | Yes | User's password |

**Example Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1Ni...",
    "refreshToken": null
  }
}
```

---

## 2. Meditation Module (`/meditations`)

### AI-Generate Routine
**POST** `/meditations/generate`  
*Description:* Generates a personalized meditation plan using AI.  
*Note:* Requires **PREMIUM** subscription.

**Request Body:**
| Field | Type | Required | Description |
| :--- | :--- | :--- | :--- |
| `stressLevel` | String | Yes | e.g., "High", "Moderate" |
| `totalDurationDays` | Integer | Yes | Plan length in days |
| `preference` | FocusArea | Yes | Primary goal (Enum) |

**Example Response (200 OK):**
```json
{
  "success": true,
  "data": {
    "id": 501,
    "title": "Calm Waters Path",
    "description": "A routine designed for stress relief.",
    "type": "ANXIETY",
    "sessions": [
      { "id": 1, "title": "Grounding", "script": "Close your eyes...", "durationMinutes": 5 }
    ]
  }
}
```

---

### Get My Routines
**GET** `/meditations`  
*Description:* Returns a paginated list of the user's routines.

**Query Parameters:**
*   `page` (Integer, default 0)
*   `size` (Integer, default 10)

---

## 3. Breathing Module (`/breathing`)

### Get Today's Schedule
**GET** `/breathing/today`  
*Description:* Returns exercises due within the next 30 minutes based on user settings.

**Example Response (200 OK):**
```json
{
  "success": true,
  "data": [
    {
      "id": 42,
      "name": "Box Breathing",
      "category": "CALMING",
      "pattern": { 
        "inhaleSeconds": 4, 
        "holdSeconds": 4, 
        "exhaleSeconds": 4, 
        "holdAfterExhale": 4 
      }
    }
  ]
}
```

---

### Log Session Completion
**POST** `/breathing/{id}/complete`  
*Description:* Records that a user finished an exercise.

**Query Parameters:**
| Parameter | Type | Required | Description |
| :--- | :--- | :--- | :--- |
| `durationSeconds` | Integer | Yes | Time spent on the exercise |

---

## 4. Notification Module (`/notifications`)

### Register Device Token
**POST** `/notifications/token`  
*Description:* Registers a Firebase FCM token for the current device.

**Query Parameters:**
| Parameter | Type | Required | Description |
| :--- | :--- | :--- | :--- |
| `token` | String | Yes | The FCM token from the device |
| `deviceType` | String | No | e.g., "iOS", "Android" |

---

### Update Preferences
**PUT** `/notifications/preferences`  
*Description:* Updates user notification settings and quiet hours.

**Request Body:**
| Field | Type | Required | Format |
| :--- | :--- | :--- | :--- |
| `quietHoursStart` | String | No | "HH:mm:ss" |
| `quietHoursEnd` | String | No | "HH:mm:ss" |
| `breathingEnabled` | Boolean | Yes | |
| `meditationEnabled` | Boolean | Yes | |

---

## 5. Subscription Module (`/subscription`)

### List Plans
**GET** `/subscription/plans`  
*Description:* Lists all available subscription levels and prices.

---

### Get Status
**GET** `/subscription/status`  
*Description:* Returns the current user's `SubscriptionStatus` enum.

---

## ⚠️ Error Responses

SerenityAI uses standard HTTP status codes. When `success` is `false`, the following errors may occur:

| Code | Error Name | Meaning |
| :--- | :--- | :--- |
| **401** | `UNAUTHORIZED` | Token is missing, invalid, or expired. |
| **403** | `SUBSCRIPTION_REQUIRED` | Feature restricted to Premium members. Includes `upgradeUrl`. |
| **403** | `ACCESS_DENIED` | Attempting to access a resource owned by another user. |
| **404** | `NOT_FOUND` | Resource ID does not exist. |
| **429** | `TOO_MANY_REQUESTS` | Rate limit exceeded (Auth endpoints). |
| **500** | `INTERNAL_ERROR` | Generic server error. Minimal details returned for security. |

**Example 403 Subscription Required:**
```json
{
  "success": false,
  "error": "SUBSCRIPTION_REQUIRED",
  "message": "This feature is exclusive for Premium members.",
  "upgradeUrl": "/api/subscription/plans"
}
```
