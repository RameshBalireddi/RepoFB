package post.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import post.Enum.FriendshipStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "followers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Followers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserProfile user;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private UserProfile follower;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus requestStatus;

    private LocalDateTime requestDate;
    private LocalDateTime acceptDate;

    public Followers(UserProfile user, UserProfile follower, FriendshipStatus requestStatus, LocalDateTime requestDate) {
        this.user = user;
        this.follower = follower;
        this.requestStatus = requestStatus;
        this.requestDate = requestDate;
    }



}
