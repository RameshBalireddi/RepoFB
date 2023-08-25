package post.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import post.Entities.UserProfile;

@Data
public class FriendResponse {

    private int userId;
    private String userName;
    private String email;
    private String profilePicPath;


    public FriendResponse(int id, String name, String email, String profilePicPath) {
        this.userId = id;
        this.userName = name;
        this.email = email;
        this.profilePicPath = profilePicPath;
    }
}
