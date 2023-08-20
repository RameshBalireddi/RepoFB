package post.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import post.APIResponse.APIResponse;
import post.Repositories.UserFriendRepository;
import post.Security.UserIdContextHolder;
import post.Service.UserFriendService;

@RestController
public class UserFriendController {
           @Autowired
           private UserFriendService userFriendService;

           @Autowired
           UserFriendRepository userFriendRepository;

    @PostMapping("/sentFriendRequest/{requestId}")
    public ResponseEntity<APIResponse> sentFriendRequest(@PathVariable int requestId) {

                int senderId=UserIdContextHolder.getUserId();

                 return userFriendService.sentFriendRequest(senderId,requestId);
             }
    @GetMapping("/pendingRequests")
    public ResponseEntity<APIResponse> getPendingRequests() {
        int receiverId=UserIdContextHolder.getUserId();
       return  userFriendService.getPendingRequests(receiverId);

    }
    @PutMapping("/friendRequest/{requestId}")
    public ResponseEntity<APIResponse> handleFriendRequest(
                                                 @PathVariable int requestId,
                                                 @RequestParam(required = false,defaultValue = "accept") String action) {
        int receiverId=UserIdContextHolder.getUserId();
        return userFriendService.handleFriendRequest(receiverId, requestId, action);

    }

    @GetMapping("/friendsList")
    public APIResponse getFriends() {
         int userId= UserIdContextHolder.getUserId();
            return  userFriendService.getFriendsByUserId(userId);
    }








}
