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
import java.util.Arrays;
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



    public ResponseEntity<APIResponse> uploadImage(String path, MultipartFile file, int userId) {
        try {
            Optional<UserProfile> userOptional = userProfileRepository.findById(userId);
            if (userOptional.isEmpty()) {
                return APIResponse.errorNotFound("User not found");
            }

            UserProfile userProfile = userOptional.get();
            String name = file.getOriginalFilename();
            String extension = getFileExtension(name);

            if (!isImageExtension(extension)) {
                return APIResponse.errorBadRequest("Only image files are allowed");
            }

            String filePath = path + File.separator + UUID.randomUUID() + "_" + name;
            Path directoryPath = Paths.get(path);

            if (!Files.exists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }

            userProfile.setProfilePicPath(filePath);
            userProfileRepository.save(userProfile);
            Files.copy(file.getInputStream(), Paths.get(filePath));

            return APIResponse.success("Profile pic uploaded in Local successfully", filePath);
        } catch (IOException e) {
            return
                    APIResponse.errorBadRequest("Failed to upload profile pic");
        }
    }
    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex + 1);
        }
        return "";
    }

    private boolean isImageExtension(String extension) {
        // You can customize this list with supported image extensions
        String[] imageExtensions = {"jpg", "jpeg", "png"};
        return Arrays.stream(imageExtensions).anyMatch(ext -> ext.equalsIgnoreCase(extension));
    }

    public ResponseEntity  getProfilePicture(int userId) {
        try {
            Optional<UserProfile> userProfile = userProfileRepository.findById(userId);
            if (userProfile.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("user not found");
            }
            String profilePicPath = userProfile.get().getProfilePicPath();
            if (profilePicPath == null || profilePicPath.isBlank())
                return APIResponse.errorBadRequest("user don't have profile pic");
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
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.errorNotFound("image not found")).getBody();
        }

        UserProfile userProfile = userProfileRepository.findById(userId).orElse(null);
        if (userProfile==null) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.errorNotFound("user not found")).getBody();
        }
        String name = file.getOriginalFilename();
        String extension = getFileExtension(name);

        if (!isImageExtension(extension)) {
            return APIResponse.errorBadRequest("Only image files are allowed");
        }

        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String profileURL = (String) uploadResult.get("secure_url");
        userProfile.setProfileURL(profileURL);
        userProfileRepository.save(userProfile);

        return APIResponse.uploadSuccess("Image uploaded in cloudinary successfully.");
    }

    public ResponseEntity<APIResponse>  getPictureInCloud(int userId) {

        Optional<UserProfile> userProfile=userProfileRepository.findById(userId);
        if(userProfile.isEmpty()){
            return  APIResponse.errorNotFound("user not found");
            }
         String  url=  userProfile.get().getProfileURL();
        if( url==null  || url.isBlank()){
            return  APIResponse.errorNotFound("URL not found  this user have no url");
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




