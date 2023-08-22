package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import post.APIResponse.APIResponse;
import post.Entities.Notification;
import post.Repositories.NotificationRepo;
import post.Responses.NotificationResponse;
import post.Security.GetUser;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    NotificationRepo notificationRepository;

    public ResponseEntity<APIResponse> getAllNotifications(boolean all) {
        List<Notification> notifications;
        if (all) {
            notifications = notificationRepository.findAll();
        } else {
            notifications = notificationRepository.findByUserId(GetUser.getUserId());
        }
        if (notifications.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(APIResponse.error("Notifications are not found").getBody());
        }

        List<NotificationResponse> notificationResponses = notifications.stream()
                .map(n -> new NotificationResponse(n.getId(), n.getUser().getId(), n.getNotification(), n.getNotificationReceivedAt()))
                .collect(Collectors.toList());

        return APIResponse.success("Notifications:", notificationResponses);
    }
}
