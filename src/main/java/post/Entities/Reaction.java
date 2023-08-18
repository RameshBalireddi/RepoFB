package post.Entities;


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
@Table(name="likes")
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name="userId")
    private UserProfile user;

    @ManyToOne
    @JoinColumn(name = "postId",referencedColumnName = "id")
    private Post post;


    @NotNull
    @Column(name="liked")
    private boolean liked=true;

    @Column(name="likedAt")
    private LocalDateTime likedAt;


}

