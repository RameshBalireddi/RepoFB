package post.Controllers;

import jakarta.validation.Valid;
import org.hibernate.annotations.NotFound;
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

    @PostMapping("/add")
    public ResponseEntity<String> addPost(@Valid @RequestBody Post post) {
        post.setCreateAt(LocalDateTime.now());
        postRepository.save(post);
        return ResponseEntity.ok("post added successfully");
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<?> getAllPosts(@PathVariable int userId) {
        ResponseEntity<?> responseEntity = postService.getAllPosts(userId);

        if (responseEntity.getStatusCode() == HttpStatus.NOT_FOUND) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Posts not found");
        }

        List<PostResponse> postResponse = (List<PostResponse>) responseEntity.getBody();
        return ResponseEntity.ok(postResponse);
    }

}



