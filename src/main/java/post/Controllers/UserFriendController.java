package post.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import post.DTO.FriendResponse;
import post.Entities.UserFriend;
import post.Repositories.UserFriendRepository;
import post.Service.UserFriendService;

import java.util.List;

@RestController
public class UserFriendController {
           @Autowired
           private UserFriendService userFriendService;

           @Autowired
           UserFriendRepository userFriendRepository;

    @PostMapping("/sentFriendRequest")
    public ResponseEntity<?> sentFriendRequest(@Valid @RequestBody UserFriend userFriend) {
        try {
            ResponseEntity<?> result = userFriendService.sentFriendRequest(userFriend);
            if(result!=null)
                return  ResponseEntity.ok(result.getBody());
        }catch (Exception e){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("please enter valid id  "+e.getMessage());
        }

      return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("unable sent the friend request");


    }
    @GetMapping("/pendingRequests/{receiverId}")
    public ResponseEntity<?> getPendingRequests(@PathVariable int receiverId) {

        ResponseEntity<?>result=   userFriendService.getPendingRequests(receiverId);
        if(result!=null)
            return ResponseEntity.ok(result.getBody());
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("unable to find the pending requests");

    }
    @PutMapping("/friendRequest/{receiverId}/{requestId}")
    public ResponseEntity<?> handleFriendRequest(@PathVariable int receiverId,
                                                 @PathVariable int requestId,
                                                 @RequestParam(required = false,defaultValue = "accept") String action) {
        ResponseEntity<?>result= userFriendService.handleFriendRequest(receiverId, requestId, action);
        if(result!=null)
            return  ResponseEntity.ok(result.getBody());
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("not identify the that request");
    }

    @GetMapping("/friendsList/{userId}")
    public ResponseEntity<?> getFriends(@PathVariable int userId) {
        try {
            ResponseEntity<List<FriendResponse>> response = (ResponseEntity<List<FriendResponse>>) userFriendService.getFriendsByUserId(userId);
            List<FriendResponse> friendsList = response.getBody();

            if (friendsList == null || friendsList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No friends found for the user.");
            }
            return ResponseEntity.ok(friendsList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching friends: " + e.getMessage());
        }
    }








}
