package post.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPostResponse {

    private int userId;
    private String userName;
    private String userEmail;
    private List<PostResponse> postResponses;


}