package post.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
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
    private User sender;

    @ManyToOne
    @JoinColumn(name = "user_id_receiver")
    private User receiver;

    @Pattern(regexp = "^(pending|accepted|rejected)$", message = "Note status must be 'pending' or 'accepted' or 'rejected'")
    private String status;

    @Column(name ="request_date")
    private LocalDateTime request_date;
    @Column(name ="acceptance_date")
    private LocalDateTime acceptance_date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRequest_date() {
        return request_date;
    }

    public void setRequest_date(LocalDateTime request_date) {
        this.request_date = request_date;
    }

    public LocalDateTime getAcceptance_date() {
        return acceptance_date;
    }

    public void setAcceptance_date(LocalDateTime acceptance_date) {
        this.acceptance_date = acceptance_date;
    }
}
