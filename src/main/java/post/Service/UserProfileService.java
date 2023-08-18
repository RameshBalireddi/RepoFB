package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import post.APIResponse.APIResponse;
import post.DTO.UserDTO;
import post.Entities.UserProfile;
import post.Repositories.UserProfileRepository;
import post.Responses.UserResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {

    @Autowired
    UserProfileRepository userProfileRepository;

   @Autowired
    PasswordEncoder passwordEncoder;

    public ResponseEntity<APIResponse> addUser(UserDTO userDTO) {
        try {

            UserProfile userProfile = new UserProfile();
            userProfile.setName(userDTO.getName());
            userProfile.setEmail(userDTO.getEmail());
            String password = passwordEncoder.encode(userDTO.getPassword());
            userProfile.setPassword(password);
            userProfileRepository.save(userProfile);
            return APIResponse.success("User added successfully", userDTO);
        } catch (Exception e) {
            return APIResponse.error("Failed to add user Please enter valid details.");
        }
    }



    public ResponseEntity<APIResponse> getAllUsers() {
        List<UserProfile> users = userProfileRepository.findByActive(true);
         if(users.isEmpty()){
             return  APIResponse.error("users not found");
         }
         List < UserResponse> userResponses=new ArrayList<>();
        for (UserProfile user:users){
            int id=user.getId();
            String name=user.getName();
            String email= user.getEmail();;
            UserResponse userResponse=new UserResponse(id,name,email);
         userResponses.add(userResponse);
        }

        return APIResponse.success("users are : ", userResponses);
    }

    public APIResponse deleteUserById(int userId) {
        Optional<UserProfile> userOptional = userProfileRepository.findById(userId);

        if (userOptional.isPresent()) {
            UserProfile user = userOptional.get();
            user.setActive(false);
            userProfileRepository.save(user);
           return APIResponse.success("User deleted successfully", user.getName()).getBody();
        } else {
            return APIResponse.error("User not found").getBody();
        }
    }


        public APIResponse updateStatusById(int userId) {
        Optional<UserProfile> user=  userProfileRepository.findById(userId);
        if (user.isEmpty()){
            return APIResponse.error("user not found").getBody();
        }
        UserProfile user1=user.get();
     if( user1.isActive()==true){
         user1.setActive(false);
         userProfileRepository.save(user1);
         return APIResponse.success("user status changed to inactive successfully ",user1.getName()).getBody();
     }
     else
         user1.setActive(true);
         userProfileRepository.save(user1);
         return APIResponse.success("user status changed to active successfully ",user1).getBody();
    }




}
