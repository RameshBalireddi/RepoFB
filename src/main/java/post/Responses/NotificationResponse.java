package post.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class NotificationResponse {

    private  int notificationId;

    private  int userId;

    private String notification;

    private LocalDateTime notificationReceivedAt;
}
