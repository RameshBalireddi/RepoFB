package post.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentReplyResponse {

    private int commentId;

    private int userId;

    private String reply;

    private int commentReplyId;

    private LocalDateTime replyAt;



}
