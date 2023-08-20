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
         try {
             return reactionService.reactPost(reactUserId, postId, reaction);
         }catch (Exception e) {
             return  APIResponse.error( e.getMessage());
         }
  }

  @PutMapping("/{postId}")
  public APIResponse changeReactionByUser(@PathVariable int postId){
      int userId= UserIdContextHolder.getUserId();
      return reactionService.changeReactionForPost(postId,userId);
  }

  @GetMapping("/reactionsList")
    public  APIResponse getAllReactions(){
      try{
      return  reactionService.getAllReactions();
  }catch (Exception e) {
        return APIResponse.error( e.getMessage()).getBody();
    }
  }
  @GetMapping("reactions")
  public  APIResponse getAllReactionsById(){
      try{
          return  reactionService.getAllReactionsById();
      }catch (Exception e) {
          return APIResponse.error( e.getMessage()).getBody();
      }
  }

}

