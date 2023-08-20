package post.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentReplyDTO {
   @NotNull
   private  int commentId;
   @NotNull
    private String commentReply;

}
