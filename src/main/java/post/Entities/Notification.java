package post.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserProfile user;
    private String notification;
    private LocalDateTime notificationReceivedAt;

    public Notification(UserProfile postUser, String s, LocalDateTime now) {

        this.user=postUser;
        this.notification=s;
        this.notificationReceivedAt=now;
    }
}
