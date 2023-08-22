package post.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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
@Component
public class FileService {

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    Cloudinary cloudinary;



    public ResponseEntity<APIResponse>  uploadImage(String path, MultipartFile file, int userId) {
        try {
            Optional<UserProfile> userOptional = userProfileRepository.findById(userId);
            if(userOptional.isEmpty()){
                return APIResponse.error("user not found");
            }

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
                return APIResponse.success("Profile pic uploaded successfully", filePath);


        } catch (IOException e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error("failed to upload profile pic")).getBody();
        }
    }

    public ResponseEntity  getProfilePicture(int userId) {
        try {
            Optional<UserProfile> userProfile = userProfileRepository.findById(userId);
            if (userProfile.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");
            }
            String profilePicPath = userProfile.get().getProfilePicPath();
            if (profilePicPath == null || profilePicPath.isBlank())
                return APIResponse.error("user don't have profile pic");
            try {
                byte[] imageContent = Files.readAllBytes(Paths.get(profilePicPath));
                ByteArrayResource resource = new ByteArrayResource(imageContent);
                return ResponseEntity.ok()
                        .header("Content-Disposition", "attachment; filename=profile_picture.jpg")
                        .header("Content-Type", MediaType.IMAGE_PNG_VALUE)
                        .body(resource.getByteArray());
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }

        } catch (Exception e) {
            System.out.printf(e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }



    public ResponseEntity<APIResponse> uploadImageInCloud(MultipartFile file, int userId) throws IOException {
        if (file == null || file.isEmpty()) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("file not found")).getBody();
        }

        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(userId);
        if (!userProfileOptional.isPresent()) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("user not found")).getBody();
        }
        UserProfile user = userProfileOptional.get();
        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String profileURL = (String) uploadResult.get("secure_url");
        user.setProfileURL(profileURL);
        userProfileRepository.save(user);

        return APIResponse.uploadSuccess("File uploaded successfully.");
    }

    public ResponseEntity<APIResponse>  getPictureInCloud(int userId) {

        Optional<UserProfile> userProfile=userProfileRepository.findById(userId);
        if(userProfile.isEmpty()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("user not found")).getBody();
            }
         String  url=  userProfile.get().getProfileURL();
        if( url==null  || url.isBlank()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("URL not found  this user have no url")).getBody();
        }
        return APIResponse.success("url :",url);

    }

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dnwhtwtmb",
                "api_key", "466345793428852",
                "api_secret", "hMMtUgqm39OqjEBJKPC7AJXnErc"
        ));
    }

}




