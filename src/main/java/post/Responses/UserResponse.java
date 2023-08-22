package post.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.DeleteMapping;

@Data
@AllArgsConstructor
public class UserResponse {

 private   int userId;

 private String name;

 private String email;

 private String profilePicPath;

 private String profilePicCloudUrl;

}
