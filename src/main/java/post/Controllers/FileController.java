package post.Controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import post.APIResponse.APIResponse;
import post.Entities.UserProfile;
import post.Repositories.UserProfileRepository;
import post.Security.UserIdContextHolder;
import post.Service.FileService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@Component
public class FileController{
       @Value("${project.image}")
    private String path;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dnwhtwtmb",
                "api_key", "466345793428852",
                "api_secret", "hMMtUgqm39OqjEBJKPC7AJXnErc"
        ));
    }

       @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public APIResponse uploadProfilePicture(
            @RequestParam("image") MultipartFile file
            ) throws IOException {
       int userId= UserIdContextHolder.getUserId();
        String uploadPath = "/home/developer/SpringWorkspace/postFB/profilePics"; // Specify the upload path
        return fileService.uploadImage(uploadPath, file, userId);
    }


    @GetMapping("/picture")
    public ResponseEntity downloadProfilePicture() {
        try {
            int userId=UserIdContextHolder.getUserId();
            Optional<UserProfile> userProfile = userProfileRepository.findById(userId);
            if (userProfile.isEmpty()) throw new Exception("user not found");
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


    @PostMapping("/uploadInCloud")
    public APIResponse uploadInCloud(
            @RequestParam("image") MultipartFile file
    ) throws IOException {
        int userId= UserIdContextHolder.getUserId();

        return fileService.uploadImageInCloud( file, userId);
    }

    @GetMapping("/pictureInCloud")
    public String redirectToProfilePicture() {
        int userId = UserIdContextHolder.getUserId();
        String profilePictureURL = String.valueOf(fileService.getPictureInCloud(userId));
        // Return the URL for redirection
        return "redirect:" + profilePictureURL;
    }



}
