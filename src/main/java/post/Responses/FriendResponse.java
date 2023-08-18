package post.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import post.Entities.UserProfile;

@Data
public class FriendResponse {

    private int id;
    private String name;
    private String email;
    private String profilePicPath;


    public FriendResponse(int id, String name, String email, String profilePicPath) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profilePicPath = profilePicPath;
    }
}
