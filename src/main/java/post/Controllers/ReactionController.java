package post.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import post.APIResponse.APIResponse;
import post.Security.GetUser;
import post.Service.ReactionService;

@RestController
public class ReactionController {

  @Autowired
    ReactionService reactionService;


  @PostMapping("/reaction/{postId}")
  public ResponseEntity<APIResponse> likePost(@PathVariable int postId, @RequestParam(required = false,defaultValue = "true")  boolean reaction) {
         int reactUserId=GetUser.getUserId();
         try {
             return reactionService.reactPost(reactUserId, postId, reaction);
         }catch (Exception e) {
             return ResponseEntity.status(HttpStatus.NO_CONTENT).body(APIResponse.error( e.getMessage())).getBody();
         }
  }

  @PutMapping("/{postId}")
  public ResponseEntity<APIResponse> changeReactionByUser(@PathVariable int postId){
      int userId= GetUser.getUserId();
      return reactionService.changeReactionForPost(postId,userId);
  }

  @GetMapping("/reactionsList")
    public  ResponseEntity<APIResponse> getAllReactions(){
      try{
      return  reactionService.getAllReactions();
  }catch (Exception e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(APIResponse.error( e.getMessage())).getBody();
    }
  }
  @GetMapping("reactions")
  public  ResponseEntity<APIResponse> getAllReactionsById(){
      try{
          return  reactionService.getAllReactionsById();
      }catch (Exception e) {
          return ResponseEntity.status(HttpStatus.NO_CONTENT).body(APIResponse.error( e.getMessage())).getBody();
      }
  }

}

