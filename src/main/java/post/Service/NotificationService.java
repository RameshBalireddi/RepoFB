package post.Service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import post.APIResponse.APIResponse;
import post.Entities.Notification;
import post.Repositories.NotificationRepo;
import post.Responses.NotificationResponse;
import post.Security.ObjectUtil;
import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepo notificationRepository;

    private static final Logger logger = Logger.getLogger(NotificationService.class.getName());

    @PostConstruct
    private void configureLogger() {
        try {
            FileHandler fileHandler = new FileHandler("/home/developer/SpringWorkspace/postFB/src/main/resources/info.log");
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResponseEntity<APIResponse> getAllNotifications(boolean all) {
        logger.info("Started retrieving notifications.");
        List<Notification> notifications;
        if (all) {
            notifications = notificationRepository.findAll();
        } else {
            notifications = notificationRepository.findByUserId(ObjectUtil.getUserId());
        }
        if (notifications.isEmpty()) {
            logger.warning("notifications not found ");
            return
                    APIResponse.errorNotFound("Notifications are not found");
        }
        logger.fine("notifications receiving .");
        List<NotificationResponse> notificationResponses = notifications.stream()
                .map(n -> new NotificationResponse(n.getId(), n.getUser().getId(), n.getNotification(), n.getNotificationReceivedAt()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(APIResponse.success("Notifications:", notificationResponses)).getBody();
    }
}
