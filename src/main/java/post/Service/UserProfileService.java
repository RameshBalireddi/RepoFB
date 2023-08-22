package post.Service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import post.APIResponse.APIResponse;
import post.DTO.UserDTO;
import post.Entities.Post;
import post.Entities.UserProfile;
import post.Repositories.PostRepository;
import post.Repositories.UserProfileRepository;
import post.Responses.PostResponse;
import post.Responses.UserPostResponse;
import post.Responses.UserResponse;
import post.Responses.UserWithPostCountResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserProfileService {

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public ResponseEntity<APIResponse> addUser(@Valid UserDTO userDTO) {
        try {
            UserProfile userProfile = new UserProfile();
            userProfile.setName(userDTO.getName());
            userProfile.setEmail(userDTO.getEmail());
            String password = passwordEncoder.encode(userDTO.getPassword());
            userProfile.setPassword(password);
            userProfileRepository.save(userProfile);
            return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("User added successfully", userDTO).getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error("Failed to add user Please enter valid details.").getBody());
        }
    }

    public ResponseEntity<APIResponse> getAllUsers() {
        List<UserProfile> users = userProfileRepository.findByActive(true);
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("users not found").getBody());
        }
        List<UserResponse> userResponses=users.stream()
                .map(u->new UserResponse(u.getId(),
                        u.getName(),
                        u.getEmail(),
                        u.getProfilePicPath(),
                        u.getProfileURL())).collect(Collectors.toList());
        return APIResponse.success("users are : ", userResponses);
    }

    public ResponseEntity<APIResponse> deleteUserById(int userId) {
        Optional<UserProfile> userOptional = Optional.ofNullable(userProfileRepository.findByIdAndActive(userId, true));
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("user not found")).getBody();
        }

        if (userOptional.isPresent()) {
            UserProfile user = userOptional.get();
            user.setActive(false);
            userProfileRepository.save(user);
            return APIResponse.success("User deleted successfully", user.getName());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("user not found")).getBody();
        }
    }

    public ResponseEntity<APIResponse> updateStatusById(int userId) {
        Optional<UserProfile> user = userProfileRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("user not found")).getBody();
        }
        UserProfile user1 = user.get();
        if (user1.isActive() == true) {
            user1.setActive(false);
            userProfileRepository.save(user1);
            return APIResponse.success("user status changed to inactive successfully ", user1.getName());
        }
        user1.setActive(true);
        userProfileRepository.save(user1);
        return APIResponse.success("user status changed to active successfully ", user1.getName());
    }
    public ResponseEntity<APIResponse> getUserDetailsAndPostsCount() {

        List<UserWithPostCountResponse> userWithPostCountResponses = userProfileRepository.getUsersWithPostCount();
        if (userWithPostCountResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("user profiles are not found")).getBody();
        }
        return APIResponse.success("response :", userWithPostCountResponses);
    }

    public ResponseEntity<APIResponse> getAllUsersWithPosts() {
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        if (userProfiles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("Users are not found")).getBody();
        }
        List<UserPostResponse> userPostResponses = new ArrayList<>();
        for (UserProfile userProfile : userProfiles) {
            int id = userProfile.getId();
            String name = userProfile.getName();
            String email = userProfile.getEmail();
            List<Post> posts = postRepository.findByUserId(id);
            List<PostResponse> postResponseList = posts.stream()
                    .map(post -> post == null ? null : new PostResponse(post.getId(), post.getPostText(), id))
                    .collect(Collectors.toList());
            userPostResponses.add(new UserPostResponse(id, name, email, postResponseList));
        }
        if (userPostResponses.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("posts are not found")).getBody();
        }
        return APIResponse.success("Responses are:", userPostResponses);
    }


}


