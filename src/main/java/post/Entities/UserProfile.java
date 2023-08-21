package post.Entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    @NotNull
    private  String name;

    @Email
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;


    private  String profilePicPath;

    private String profileURL;

    private boolean active=true;



}
