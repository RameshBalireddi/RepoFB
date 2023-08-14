package post.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import post.Entities.Like;
import post.Repositories.LikeRepository;
import post.Service.LikeService;

import java.time.LocalDate;

@RestController
public class LikeController {

  @Autowired
    LikeService likeService;


    @PostMapping("/like")
    public ResponseEntity<String> likePost( @RequestBody @Valid Like like){

       return (ResponseEntity<String>) likeService.likeAPost(like);



      }
    }

