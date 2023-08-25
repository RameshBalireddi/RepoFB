package post.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import post.APIResponse.APIResponse;
import post.Security.ObjectUtil;
import post.Service.FollowingService;

@RestController
@RequestMapping("/follow")
public class FollowersController {


    @Autowired
    FollowingService followingService;


    @PostMapping("/sentRequest/{requestId}")
    public ResponseEntity<APIResponse> sentRequest(@PathVariable int requestId){
      return   followingService.sentRequest(requestId);
    }

    @PutMapping("/accept/{requestId}")
    public ResponseEntity<APIResponse> acceptRequest(@PathVariable int requestId){
        return   followingService.acceptRequest(requestId);

    }

    @GetMapping("followers")
    public ResponseEntity<APIResponse> getAllFollowers(){
        return   followingService.getALlFollowers();

    }

    @GetMapping("following")
    public ResponseEntity<APIResponse> getAllFollowing(){
        return   followingService.getALlFollowing();

    }

    @PutMapping("reject/{requestId}")
    public ResponseEntity<APIResponse> rejectRequest(@PathVariable int requestId){

        return  followingService.rejectRequest(requestId);
    }

    @GetMapping("pending/requests")
    public ResponseEntity<APIResponse> pendingRequests(){
      int  receiverId= ObjectUtil.getUserId();
        return  followingService.pendingRequests(receiverId);
    }

    @GetMapping("pending/requestsList")
    public ResponseEntity<APIResponse> getALLPendingRequests(){
              return  followingService.AllPendingRequests();
    }

}
