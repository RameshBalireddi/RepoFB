package post.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import post.APIResponse.APIResponse;
import post.DTO.UserDTO;
import post.Repositories.UserProfileRepository;
import post.Security.GetUser;
import post.Service.UserProfileService;

@RestController
@RequestMapping("user")
public class UserProfileController {

    @Autowired
    UserProfileRepository userRepository;
    @Autowired
    UserProfileService userProfileService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addUser(@RequestBody @Valid UserDTO userDTO) {
            return userProfileService.addUser(userDTO);
    }

    @GetMapping("/list")
    public ResponseEntity<APIResponse> getUsers() {
    return userProfileService.getAllUsers();
    }

    @PutMapping("/status")
    public ResponseEntity<APIResponse> updateUserStatus(){
        int userId= GetUser.getUserId();
        return userProfileService.updateStatusById(userId);

    }
    @DeleteMapping("")
    public ResponseEntity<APIResponse> deleteUser(){
        int userId= GetUser.getUserId();
        return userProfileService.deleteUserById(userId);
    }

    @GetMapping("/postCount")
    public ResponseEntity<APIResponse> getUserDetailsAndPostsCount(){

      return  userProfileService.getUserDetailsAndPostsCount();
    }
    @GetMapping("/posts")
    public  ResponseEntity<APIResponse> getPosts(){
        return  userProfileService.getAllUsersWithPosts();
    }

}
