package post.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import post.APIResponse.APIResponse;
import post.DTO.PostDTO;
import post.Security.UserIdContextHolder;
import post.Service.PostService;

@RestController
@RequestMapping("post")
public class PostController{


    @Autowired
    PostService postService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addPost(@Valid @RequestBody String postText) {
        int userId=UserIdContextHolder.getUserId();
       return   postService.addPost(postText,userId);
    }

    @GetMapping("/list")
    public APIResponse getAllPosts() {
        int userId=UserIdContextHolder.getUserId();
        return (APIResponse) postService.getAllPosts(userId);
    }

    @PutMapping("update/{postId}")
        public ResponseEntity<APIResponse> changePostByPostId(@PathVariable int postId,@RequestBody String postText){
       return  postService.updatePost(postId,postText);
    }

   @DeleteMapping("/{postId}")
    public APIResponse postDelete(@PathVariable int postId){
        int userId= UserIdContextHolder.getUserId();
     return    postService.deletePostById(postId,userId);

   }



}



