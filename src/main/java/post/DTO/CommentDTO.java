package post.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentDTO {


    @NotNull
    private int postId;
     @NotNull
    private String comment;

}
