package post.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentReplyRequest {

    @NotNull
    private  int commentReplyId;
    @NotNull
    private String CommentReplyText;
}
