package com.asdeveloper.BloodMate.Notifications;

import lombok.Data;

@Data
public class NotificationRequest {
    private String fcmToken;
    private String title;
    private String body;
}
