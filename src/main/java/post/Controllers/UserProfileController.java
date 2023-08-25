package post.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import post.APIResponse.APIResponse;
import post.DTO.EmailRequest;
import post.DTO.PasswordDTO;
import post.DTO.UserDTO;
import post.DTO.UserNameRequest;
import post.Repositories.UserProfileRepository;
import post.Security.ObjectUtil;
import post.Service.UserProfileService;

@RestController
@RequestMapping("user")
public class UserProfileController {
    @Autowired
    UserProfileRepository userRepository;
    @Autowired
    UserProfileService userProfileService;

    @PostMapping("/signup")
    public ResponseEntity<APIResponse> addUser(@RequestBody @Valid UserDTO userDTO) {
            return userProfileService.addUser(userDTO);
    }
    @GetMapping("/list")
    public ResponseEntity<APIResponse> getUsers() {
    return userProfileService.getAllUsers();
    }

    @PutMapping("update/email")
    public ResponseEntity<APIResponse> updateUserEmail(@RequestBody @Valid EmailRequest emailRequest) {

        return userProfileService.updateEmailByUserId(emailRequest);
    }
    @PutMapping("update/userName")
    public ResponseEntity<APIResponse> updateUserName(@RequestBody @Valid UserNameRequest userNameRequest) {

        return userProfileService.updateUserName(userNameRequest);
    }
    @DeleteMapping("")
    public ResponseEntity<APIResponse> deleteUser(){
        int userId= ObjectUtil.getUserId();
        return userProfileService.deleteUserById(userId);
    }
    @GetMapping("/posts/count")
    public ResponseEntity<APIResponse> getUserDetailsAndPostsCount(){
      return  userProfileService.getUserDetailsAndPostsCount();
    }


    @GetMapping("/posts")
    public  ResponseEntity<APIResponse> getPosts(){
        return  userProfileService.getAllUsersWithPosts();
    }

    @PutMapping("/update/password")
    public ResponseEntity<APIResponse> updatePassword(@RequestBody @Valid PasswordDTO passwordDTO){
        return userProfileService.updateUserPassword(passwordDTO);
    }


}
