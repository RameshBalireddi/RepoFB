package post.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import post.Entities.Like;
import post.Service.LikeService;

@RestController
public class LikeController {

  @Autowired
    LikeService likeService;


  @PostMapping("/like")
  public ResponseEntity<String> likePost(@RequestBody @Valid Like like) {
    try {
      ResponseEntity<String> response = (ResponseEntity<String>) likeService.likeAPost(like);
      return ResponseEntity.ok(response.getBody());
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error liking the post: " + e.getMessage());
    }
  }

}

