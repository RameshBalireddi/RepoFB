package post.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import post.APIResponse.APIResponse;
import post.DTO.PostRequest;
import post.Security.ObjectUtil;
import post.Service.PostService;

@RestController
@RequestMapping("post")
public class PostController{


    @Autowired
    PostService postService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addPost( @RequestBody @Valid PostRequest postRequest) {
        int userId= ObjectUtil.getUserId();
       return   postService.addPost(postRequest,userId);
    }

    @GetMapping("/list/all")
    public ResponseEntity<APIResponse>  getAllPosts() {

        return  postService.getAllUserPosts();
    }

    @GetMapping("/following")
    public ResponseEntity<APIResponse>  getFriendsPosts() {
                return  postService.getFollowingPosts();
    }
    @GetMapping("/list")
    public ResponseEntity<APIResponse>  getOwnPosts() {
                return  postService.getOwnPosts();
    }

    @PutMapping("update/{postId}")
        public ResponseEntity<APIResponse>  changePostByPostId(@PathVariable int postId, @RequestBody String postText){
       return  postService.updatePost(postId,postText);
    }

   @DeleteMapping("/{postId}")
    public ResponseEntity<APIResponse>  postDelete(@PathVariable int postId){
        int userId= ObjectUtil.getUserId();
     return    postService.deletePostById(postId,userId);

   }



}



