package post.Service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import post.APIResponse.APIResponse;
import post.DTO.EmailRequest;
import post.DTO.PasswordDTO;
import post.DTO.UserDTO;
import post.DTO.UserNameRequest;
import post.Entities.Post;
import post.Entities.UserProfile;
import post.Repositories.PostRepository;
import post.Repositories.UserProfileRepository;
import post.Responses.PostResponse;
import post.Responses.UserPostResponse;
import post.Responses.UserResponse;
import post.Responses.UserWithPostCountResponse;
import post.Security.ObjectUtil;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserProfileService {

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

        public ResponseEntity<APIResponse> addUser(@Valid UserDTO userDTO) {
            UserProfile user=userProfileRepository.findByEmail(userDTO.getEmail());
            if(user!=null){
            return  APIResponse.errorBadRequest("Email address is already registered. give unique email");
        }
        UserProfile userProfile = new UserProfile();
        userProfile.setName(userDTO.getUserName());
        userProfile.setEmail(userDTO.getEmail());
        userProfile.setFlag(true);

            if (!isValidPassword(userDTO.getPassword())) {
                return APIResponse.errorBadRequest("Password must contain at least one number, one capital letter, and one special character.");
            }
        String encodedPassword = bCryptPasswordEncoder.encode(userDTO.getPassword());
        userProfile.setPassword(encodedPassword);
        userProfileRepository.save(userProfile);

        APIResponse response = new APIResponse();
        response.setSuccess(true);
        response.setMessage("User added successfully");
        response.setData(userDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    private boolean isValidPassword(String password) {
        // Regular expression to check for at least one number, one capital letter, and one special character
        String passwordRegex = "^(?=.*\\d)(?=.*[A-Z])(?=.*[!@#$%^&*]).*$";
        return password.matches(passwordRegex);
    }

    public ResponseEntity<APIResponse> getAllUsers() {
        List<UserProfile> users = userProfileRepository.findAll();
        if (users.isEmpty()) {
            return APIResponse.errorNotFound("users not found");
        }
        List<UserResponse> userResponses=users.stream()
                .map(u->new UserResponse(u)).collect(Collectors.toList());
        return APIResponse.success("users are : ", userResponses);
    }

    public ResponseEntity<APIResponse> deleteUserById(int userId) {
        UserProfile user = userProfileRepository.findById(userId).orElse(null);
        if (user==null) {
            return APIResponse.errorNotFound("user not found");
        }
            user.setFlag(false);
            userProfileRepository.save(user);
            return APIResponse.success("User deleted successfully", user.getName());
    }
    public ResponseEntity<APIResponse> updateEmailByUserId(EmailRequest emailRequest) {
        try {
            UserProfile user = userProfileRepository.findById(ObjectUtil.getUserId()).orElse(null);
            if (user == null) {
                return APIResponse.errorNotFound("user not found");
            }
            UserProfile email=userProfileRepository.findByEmail(emailRequest.getEmail());
            if(email!=null){
                return  APIResponse.errorBadRequest("Email address is already registered. give unique email");
            }

            user.setEmail(emailRequest.getEmail());
            userProfileRepository.save(user);

            return APIResponse.success("user email updated successfully ", user.getEmail());
        }catch (RuntimeException r){
            return APIResponse.errorBadRequest("please provide valid email");
        }
    }

    public ResponseEntity<APIResponse> updateUserName(UserNameRequest userNameRequest) {
        try {
            UserProfile user = userProfileRepository.findById(ObjectUtil.getUserId()).orElse(null);
            if (user == null) {
                return APIResponse.errorNotFound("user not found");
            }
            user.setName(userNameRequest.getUserName());
            userProfileRepository.save(user);

            return APIResponse.success("user name updated successfully ", userNameRequest.getUserName());
        }catch (RuntimeException r){
            return APIResponse.errorBadRequest("enter valid name");
        }
    }

    public ResponseEntity<APIResponse> getUserDetailsAndPostsCount() {

        List<UserWithPostCountResponse> userWithPostCountResponses = userProfileRepository.getUsersWithPostCount();
        if (userWithPostCountResponses.isEmpty()) {
            return APIResponse.errorNotFound("user profiles are not found");
        }
        return APIResponse.success("response :", userWithPostCountResponses);
    }

    public ResponseEntity<APIResponse> getAllUsersWithPosts() {
        List<UserProfile> userProfiles = userProfileRepository.findAll();
        if (userProfiles.isEmpty()) {
            return APIResponse.errorNotFound("Users are not found");
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
            return APIResponse.errorNotFound(" user with posts are empty");
        }
        return APIResponse.success("user with posts :", userPostResponses);
    }

    public ResponseEntity<APIResponse> updateUserPassword(PasswordDTO passwordDTO) {
        try {

            UserProfile userProfile = userProfileRepository.findById(ObjectUtil.getUserId()).orElse(null);
            if (userProfile == null) {
                return APIResponse.errorNotFound("user not found");
            }
            if (!bCryptPasswordEncoder.matches(passwordDTO.getOldPassword(), userProfile.getPassword())) {
                return APIResponse.errorBadRequest("Old password is did not matched.");
            }
            if(!(passwordDTO.getNewPassword()).equals(passwordDTO.getConfirmPassword())){
                return  APIResponse.errorBadRequest("new password and confirm password not matched");
            }
            if (!isValidPassword(passwordDTO.getConfirmPassword())) {
                return APIResponse.errorBadRequest("Password must contain at least one number, one capital letter, and one special character.");
            }

            String encodedPassword = passwordEncoder.encode(passwordDTO.getConfirmPassword());
            userProfile.setPassword(encodedPassword);
            userProfileRepository.save(userProfile); // Replace this with your saving logic
            return ResponseEntity.ok(APIResponse.success("Password updated successfully.", passwordDTO)).getBody();
            } catch (Exception e) {
            return
                    APIResponse.errorNotFound("An error occurred while updating the password.");
        }
    }

}


