package post.Entities;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import post.Enum.FriendshipStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="friendship")
public class UserFriend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_sender")
    private UserProfile sender;

    @ManyToOne
    @JoinColumn(name = "user_id_receiver")
    private UserProfile receiver;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    @Column(name ="request_date")
    private LocalDateTime request_date;

    @Column(name ="acceptance_date")
    private LocalDateTime acceptance_date;

}
