package post.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequest {

    @NotNull
    private int commentId;
    @NotNull
    private String commentText;
}
