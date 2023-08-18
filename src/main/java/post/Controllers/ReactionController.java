package post.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import post.APIResponse.APIResponse;
import post.Security.UserIdContextHolder;
import post.Service.ReactionService;

@RestController
public class ReactionController {

  @Autowired
    ReactionService reactionService;


  @PostMapping("/reaction/{postId}")
  public ResponseEntity<APIResponse> likePost(@PathVariable int postId,@RequestParam(required = false,defaultValue = "true")  boolean reaction) {
      int reactUserId=UserIdContextHolder.getUserId();

       return reactionService.reactPost(reactUserId,postId,reaction);

  }

  @PutMapping("/{postId}")
  public APIResponse changeReactionByUser(@PathVariable int postId){
      int userId= UserIdContextHolder.getUserId();
      return reactionService.changeReactionForPost(postId,userId);
  }


}

