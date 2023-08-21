package post.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserWithPostCountResponse {
    private int userId;
    private String userName;
    private String userEmail;
    private String profilePicPath;
    private Long postCount;
}
