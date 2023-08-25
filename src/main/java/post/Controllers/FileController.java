package post.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import post.APIResponse.APIResponse;
import post.Repositories.UserProfileRepository;
import post.Security.ObjectUtil;
import post.Service.FileService;

import java.io.IOException;


@RestController

public class FileController{
    @Value("${project.image}")
    private String path;



       @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    private FileService fileService;

    @PostMapping("/upload/profilePicture")
    public ResponseEntity<APIResponse>  uploadProfilePicture(
            @RequestParam("image") MultipartFile file
            ) throws IOException {
       int userId= ObjectUtil.getUserId();
        String uploadPath = "/home/developer/SpringWorkspace/postFB/profilePics"; // Specify the upload path
        return fileService.uploadImage(uploadPath, file, userId);
    }


    @GetMapping("profilePicture")
    public ResponseEntity downloadProfilePicture() {

      return  fileService.getProfilePicture(ObjectUtil.getUserId());
    }


    @PostMapping("/upload/profilePicture/cloudinary")
    public ResponseEntity<APIResponse> uploadInCloud(
            @RequestParam("image") MultipartFile file
    ) throws IOException {
        int userId= ObjectUtil.getUserId();

        return fileService.uploadImageInCloud( file, userId);
    }

    @GetMapping("/profilePicture/Cloudinary")
    public ResponseEntity<APIResponse>  redirectToProfilePicture() {
        int userId = ObjectUtil.getUserId();
        return fileService.getPictureInCloud(userId);
    }

}
