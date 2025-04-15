package com.asdeveloper.BloodMate.Notifications;


import com.google.firebase.messaging.*;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/notifications")
public class NotificationController {


    @PostMapping("/send")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
        try {
            Message message = Message.builder()
                .setToken(request.getFcmToken())
                .setNotification(Notification.builder()
                    .setTitle(request.getTitle())
                    .setBody(request.getBody())
                    .build())
                .putData("customKey", "customValue") // Optional
                .build();

            String response = FirebaseMessaging.getInstance().send(message);
            return ResponseEntity.ok("Notification sent: " + response);
        } catch (FirebaseMessagingException e) {
            return ResponseEntity.status(500).body("Error sending notification: " + e.getMessage());
        }
    }


}
 
