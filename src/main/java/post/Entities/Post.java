package post.Entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private int id;
    @ManyToOne
    @JoinColumn(name = "userId",referencedColumnName = "id")
    private UserProfile user;
    @NotNull
    @Column(name="post_text")
    private  String postText;
    private boolean flag=true;
    @Column(name="createdAt")
    private LocalDateTime createAt;

}
