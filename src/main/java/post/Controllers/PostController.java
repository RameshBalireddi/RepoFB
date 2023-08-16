package post.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import post.DTO.PostResponse;
import post.Entities.Post;
import post.Repositories.PostRepository;
import post.Service.PostService;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("post")
public class PostController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @PostMapping("/create")
    public ResponseEntity<String> addPost(@Valid @RequestBody Post post) {
        try {
            post.setCreateAt(LocalDateTime.now());
            postRepository.save(post);
            return ResponseEntity.ok("Post added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("An error occurred while adding the post: " + e.getMessage());
        }
    }


    @GetMapping("/list/{userId}")
    public ResponseEntity<?> getAllPosts(@PathVariable int userId) {
        try {
            ResponseEntity<?> responseEntity = postService.getAllPosts(userId);
            if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Posts not found");
            }
            List<PostResponse> postResponse = (List<PostResponse>) responseEntity.getBody();
            return ResponseEntity.ok(postResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while retrieving posts: " + e.getMessage());
        }
    }


}



