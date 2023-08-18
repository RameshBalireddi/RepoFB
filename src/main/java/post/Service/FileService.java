package post.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import post.APIResponse.APIResponse;
import post.Entities.UserProfile;
import post.Repositories.UserProfileRepository;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    Cloudinary cloudinary;

    public APIResponse uploadImage(String path, MultipartFile file, int userId) {
        try {
            Optional<UserProfile> userOptional = userProfileRepository.findById(userId);
            if (userOptional.isPresent()) {
                UserProfile userProfile = userOptional.get();
                String name = file.getOriginalFilename();
                String filePath = path + File.separator + UUID.randomUUID() + "_" + name;

                Path directoryPath = Paths.get(path);
                if (!Files.exists(directoryPath)) {
                        Files.createDirectories(directoryPath);
                }

                userProfile.setProfilePicPath(filePath);
                userProfileRepository.save(userProfile);
                Files.copy(file.getInputStream(), Paths.get(filePath));
                return APIResponse.success("Profile pic uploaded successfully", filePath).getBody();
            }
            return APIResponse.error("User not found").getBody();
        } catch (IOException e) {
            return APIResponse.error("Failed to upload profile pic").getBody();
        }
    }

    public APIResponse getProfilePicture(int userId) {
        try {
            Optional<UserProfile> userOptional = userProfileRepository.findById(userId);
            if (userOptional.isPresent()) {
                UserProfile userProfile = userOptional.get();
                String profilePicPath = userProfile.getProfilePicPath();
                byte[] fileBytes = Files.readAllBytes(Paths.get(profilePicPath));
                return APIResponse.success("Profile picture retrieved successfully", fileBytes).getBody();
            }
            return APIResponse.error("User not found").getBody();
        } catch (IOException e) {
            return APIResponse.error("Failed to retrieve profile pic").getBody();
        }
    }



    public APIResponse uploadImageInCloud(MultipartFile file, int userId) throws IOException {
        if (file == null || file.isEmpty()) {
            return APIResponse.error("File not found.").getBody();
        }

        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(userId);
        if (!userProfileOptional.isPresent()) {
            return APIResponse.error("User not found.").getBody();
        }
        UserProfile user = userProfileOptional.get();
        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String profileURL = (String) uploadResult.get("secure_url");
        user.setProfileURL(profileURL);
        userProfileRepository.save(user);

        return APIResponse.uploadSuccess("File uploaded successfully.").getBody();
    }

    public String getPictureInCloud(int userId) {

        Optional<UserProfile> userProfile=userProfileRepository.findById(userId);
        if(userProfile.isEmpty()){
            return "not Found";
            }
       String  url=   userProfile.get().getProfileURL();
        return url;

    }


}




