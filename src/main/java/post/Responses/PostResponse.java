package post.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class PostResponse {

  private int postId;
  private String postText;
  private int userId;

  public  PostResponse(int postId,String postText){

    this.postId=postId;
    this.postText=postText;
  }

}

