package post.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostResponse {

  private int  postId;
  private String post;
  private int userId;
  private int likes;
  private int comments;


}
