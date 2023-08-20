package post.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "userId")
    private UserProfile user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "postId",referencedColumnName = "id")
    private Post post;

    @Column(name="comment")
    private String comment;

    @Column(name="commentedAt")
    private LocalDateTime commentedAt;


}
