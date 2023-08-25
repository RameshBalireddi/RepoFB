package post.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.DeleteMapping;
import post.Entities.UserProfile;

@Data
@AllArgsConstructor
public class UserResponse {

 private   int userId;

 private String name;

 private String email;

 private String profilePicPath;

 private String profilePicCloudUrl;

 public UserResponse(UserProfile user) {
  this.userId = user.getId();
  this.name = user.getName();
  this.email = user.getEmail();
  this.profilePicPath = user.getProfilePicPath();
  this.profilePicCloudUrl = user.getProfileURL();
 }
}
