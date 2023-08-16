package post.Entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="replies")
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "commentId",referencedColumnName = "id")
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "replyId")
    private User user;

    @NotNull
    private String reply;

    @NotNull
    private LocalDateTime replyAt;

}
