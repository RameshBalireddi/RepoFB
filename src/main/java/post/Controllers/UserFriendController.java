package post.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import post.APIResponse.APIResponse;
import post.Repositories.UserFriendRepository;
import post.Security.GetUser;
import post.Service.UserFriendService;

@RestController
public class UserFriendController {
           @Autowired
           private UserFriendService userFriendService;

           @Autowired
           UserFriendRepository userFriendRepository;

    @PostMapping("/sentFriendRequest/{requestId}")
    public ResponseEntity<APIResponse> sentFriendRequest(@PathVariable int requestId) {

                int senderId=GetUser.getUserId();

                 return userFriendService.sendFriendRequest(senderId,requestId);
             }
    @GetMapping("/pendingRequests")
    public ResponseEntity<APIResponse> getPendingRequests(@RequestParam(required = false,defaultValue = "false")boolean all) {
        int receiverId=GetUser.getUserId();
       return  userFriendService.getPendingRequests(receiverId,all);

    }
    @PutMapping("/friendRequest/{requestId}")
    public ResponseEntity<APIResponse> handleFriendRequest(
                                                 @PathVariable int requestId,
                                                 @RequestParam(required = false,defaultValue = "accept") String action) {
        int receiverId=GetUser.getUserId();
        return userFriendService.handleFriendRequest(receiverId, requestId, action);

    }

    @GetMapping("/friendsList")
    public ResponseEntity<APIResponse> getFriends() {
         int userId= GetUser.getUserId();
            return  userFriendService.getFriendsByUserId(userId);
    }

}
