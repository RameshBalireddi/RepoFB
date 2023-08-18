package post.DTO;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import post.Entities.UserProfile;

@Data
public class FriendsDTO {

  private  int userId;

  private String name;

  private String email;

  private  String profilePicPath;

//  @Autowired
//  public FriendsDTO(UserProfile userProfile){
//    this.userId = userProfile.getId();
//    this.name=userProfile.getName();
//    this.email=userProfile.getEmail();
//    this.profilePicPath=userProfile.getProfilePicPath();
//  }

}
