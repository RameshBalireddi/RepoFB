package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import post.DTO.FriendRequestDTO;
import post.DTO.FriendResponse;
import post.Entities.User;
import post.Entities.UserFriend;
import post.Repositories.UserFriendRepository;
import post.Repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class UserFriendService {

    @Autowired
    private UserFriendRepository userFriendRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserFriendService userFriendService;
    public ResponseEntity<?> sentFriendRequest(UserFriend userFriend) {
        int senderId = userFriend.getSender().getId();
        int receiverId = userFriend.getReceiver().getId();

        if (senderId == receiverId) {
            return ResponseEntity.badRequest().body("Sender and receiver cannot be the same.");
        }

        UserFriend existingFriendship = userFriendRepository.findBySenderIdAndReceiverId(senderId, receiverId);
//        UserFriend checkSenderSide = userFriendRepository.findBySenderIdAndReceiverId(receiverId, senderId);

        if (existingFriendship == null ) {
            userFriend.setStatus("pending");
            userFriend.setRequest_date(LocalDateTime.now());
            userFriendRepository.save(userFriend);
            return ResponseEntity.ok("Friend request sent successfully.");
        }

        String friendStatus = existingFriendship.getStatus() ;
        if (friendStatus.equalsIgnoreCase("accepted")) {
            return ResponseEntity.ok("You both are already friends.");
        } else if (friendStatus.equalsIgnoreCase("rejected")) {
            userFriend.setStatus("pending");
            userFriend.setRequest_date(LocalDateTime.now());
            userFriendRepository.save(userFriend);

            return ResponseEntity.ok("again Your friend request was sent successfully");

        } else if (friendStatus.equalsIgnoreCase("pending")) {
            return ResponseEntity.ok("Friend request was already sent.");
        }

        return ResponseEntity.badRequest().body("something error occurred.");
    }


    public ResponseEntity<?> getPendingRequests(int receiverId) {
        String pending ="pending";
        List<UserFriend> pendingRequests = userFriendRepository.findPendingRequestsByReceiverAndStatus(
                receiverId, pending);
        List<FriendRequestDTO> friendRequestsList=  new LinkedList<>();
        if (pendingRequests.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body("You don't have any pending friend requests.");
        }
        for( UserFriend friend:pendingRequests){
            Optional<User> sender=userRepository.findById(friend.getSender().getId());
            FriendRequestDTO friendRequestDTO=new FriendRequestDTO();
            friendRequestDTO.setSenderId( friend.getSender().getId());
            friendRequestDTO.setStatus(friend.getStatus());
            friendRequestDTO.setSenderName(sender.get().getName());
            friendRequestsList.add(friendRequestDTO);
        }
        return ResponseEntity.ok(friendRequestsList);
    }

    public ResponseEntity<?> handleFriendRequest(int receiverId, int requestId,String requestAction) {

        if (receiverId == requestId) {
            return ResponseEntity.badRequest().body("receiver and sender cannot be the same.");
        }
        String status = "pending";
        UserFriend userFriend = userFriendRepository.findByReceiverIdAndRequestIdAndStatus(receiverId, requestId, status);
        if(userFriend==null){
         return    ResponseEntity.status(HttpStatus.NOT_FOUND).body("you haven't got any request from given user");
        }
        if (requestAction.equalsIgnoreCase("accept")) {
                userFriend.setStatus("accepted");
                userFriend.setAcceptance_date(LocalDateTime.now());
                userFriendRepository.save(userFriend);
                return ResponseEntity.ok("friend Request accepted successfully");
            }
            if (requestAction.equalsIgnoreCase("reject")) {
                userFriend.setStatus("rejected");
                userFriendRepository.save(userFriend);
                return ResponseEntity.ok("friend Request rejected successfully");
            }
            UserFriend requestStatus = userFriendRepository.findBySenderIdAndReceiverId(requestId, receiverId);
//        UserFriend checkRequestSide = userFriendRepository.findBySenderIdAndReceiverId(receiverId, requestId);
        String friendStatus = requestStatus.getStatus();
        if (friendStatus.isEmpty()) {ResponseEntity.status(HttpStatus.NOT_FOUND).body("you haven't got any request from given user");}
            if (friendStatus.equalsIgnoreCase("accepted")) {
                return ResponseEntity.ok("you both are already friends ");
            }
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body("you haven't got any request from given user");
     }

    public ResponseEntity<List<FriendResponse>> getFriendsByUserId(int userId) {
        List<Object[]> objectUsers = userFriendRepository.findFriendsById(userId);
        List<FriendResponse> userList = new ArrayList<>();

        for (Object[] user : objectUsers) {
            int id = (int) user[0];
            String name = (String) user[1];
            String email = (String) user[2];

            FriendResponse userFriend = new FriendResponse(id, name, email);
            userList.add(userFriend);
        }

        return ResponseEntity.ok(userList);
    }



}





