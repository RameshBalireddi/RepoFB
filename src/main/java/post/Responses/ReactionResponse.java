package post.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
@AllArgsConstructor
@Data
public class ReactionResponse {

    private int userId;

    private int postId;

    private int reactionId;

    private boolean like;




}
