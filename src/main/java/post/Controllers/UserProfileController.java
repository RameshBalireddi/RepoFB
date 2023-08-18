package post.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import post.APIResponse.APIResponse;
import post.DTO.UserDTO;
import post.Entities.UserProfile;
import post.Repositories.UserProfileRepository;
import post.Security.UserIdContextHolder;
import post.Service.UserProfileService;
import java.util.List;

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

    @PutMapping("status")
    public APIResponse updateUserStatus(){
        int userId= UserIdContextHolder.getUserId();
        return userProfileService.updateStatusById(userId);

    }
    @DeleteMapping("")
    public APIResponse deleteUser(){
        int userId= UserIdContextHolder.getUserId();
        return userProfileService.deleteUserById(userId);
    }





}
