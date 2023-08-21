package post.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import post.APIResponse.APIResponse;
import post.Security.GetUser;
import post.Service.PostService;

@RestController
@RequestMapping("post")
public class PostController{


    @Autowired
    PostService postService;

    @PostMapping("/add")
    public ResponseEntity<APIResponse> addPost( @RequestBody String postText) {
        if(postText==null) return  APIResponse.error("please add post text");
        int userId=GetUser.getUserId();
       return   postService.addPost(postText,userId);
    }

    @GetMapping("/list")
    public ResponseEntity<APIResponse>  getAllPosts(@RequestParam(required = false,defaultValue = "false") boolean friends,@RequestParam(required = false,defaultValue = "false") boolean own) {
        int userId= GetUser.getUserId();
        return  postService.getAllPosts(userId,friends,own);
    }

    @PutMapping("update/{postId}")
        public ResponseEntity<APIResponse>  changePostByPostId(@PathVariable int postId, @RequestBody String postText){
       return  postService.updatePost(postId,postText);
    }

   @DeleteMapping("/{postId}")
    public ResponseEntity<APIResponse>  postDelete(@PathVariable int postId){
        int userId= GetUser.getUserId();
     return    postService.deletePostById(postId,userId);

   }



}



