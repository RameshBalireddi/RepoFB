package post.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConverterNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import post.APIResponse.APIResponse;
import post.Responses.FriendResponse;
import post.DTO.UserFriendDTO;
import post.Repositories.UserFriendRepository;
import post.Security.UserIdContextHolder;
import post.Service.UserFriendService;

import java.util.List;

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
    public ResponseEntity<?> handleFriendRequest(
                                                 @PathVariable int requestId,
                                                 @RequestParam(required = false,defaultValue = "accept") String action) {
        int receiverId=UserIdContextHolder.getUserId();
        ResponseEntity<?>result= userFriendService.handleFriendRequest(receiverId, requestId, action);
        if(result!=null)
            return  ResponseEntity.ok(result.getBody());
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("not identify the that request");
    }

    @GetMapping("/friendsList")
    public ResponseEntity<APIResponse> getFriends() {
         int userId= UserIdContextHolder.getUserId();
            ResponseEntity<APIResponse> response = userFriendService.getFriendsByUserId(userId);

            return  ResponseEntity.ok(APIResponse.success("friends :",response).getBody());
    }








}
