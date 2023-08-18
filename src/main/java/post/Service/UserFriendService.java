package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import post.APIResponse.APIResponse;
import post.DTO.FriendRequestDTO;
import post.Responses.FriendResponse;
import post.Entities.UserFriend;
import post.Entities.UserProfile;
import post.Enum.FriendshipStatus;
import post.Repositories.UserFriendRepository;
import post.Repositories.UserProfileRepository;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserFriendService {

    @Autowired
    private UserFriendRepository userFriendRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private UserFriendService userFriendService;
    public ResponseEntity<APIResponse> sentFriendRequest(int senderId,int requestId) {

        if (senderId == requestId) {
            return APIResponse.error("Sender and receiver cannot be the same.");
        }
        UserProfile senderProfile = userProfileRepository.findById(senderId).orElse(null);
        UserProfile receiverProfile = userProfileRepository.findById(requestId).orElse(null);
        if (senderProfile == null || receiverProfile == null) {
            return APIResponse.error("Invalid sender or receiver ID.");
        }
        UserFriend existingFriendship = userFriendRepository.findBySenderIdAndReceiverId(senderId, requestId);

        if (existingFriendship == null) {
            UserFriend userFriend = new UserFriend();
            userFriend.setSender(senderProfile);
            userFriend.setReceiver(receiverProfile);
            userFriend.setRequest_date(LocalDateTime.now());
            userFriend.setStatus(FriendshipStatus.PENDING);
            userFriendRepository.save(userFriend);
            return APIResponse.success("Friend request sent successfully.", requestId);
        }

        if (existingFriendship.getStatus() == FriendshipStatus.ACCEPTED) {
            return APIResponse.error("You both are already friends.");
        } else if (existingFriendship.getStatus() == FriendshipStatus.REJECTED) {
            existingFriendship.setStatus(FriendshipStatus.PENDING);
            existingFriendship.setRequest_date(LocalDateTime.now());
            userFriendRepository.save(existingFriendship);
            return APIResponse.error("Your friend request was sent again successfully");
        }
        return APIResponse.error("Friend request was already sent.");
    }


    public ResponseEntity<APIResponse> getPendingRequests(int receiverId) {
        String pending ="PENDING";
        List<UserFriend> pendingRequests = userFriendRepository.findPendingRequestsByReceiverAndStatus(
                receiverId, pending);
        List<FriendRequestDTO> friendRequestsList=  new LinkedList<>();
        if (pendingRequests.isEmpty()) {
            return APIResponse.error("You don't have any pending friend requests.");
        }
        for( UserFriend friend:pendingRequests){
            Optional<UserProfile> sender=userProfileRepository.findById(friend.getSender().getId());
            FriendRequestDTO friendRequestDTO=new FriendRequestDTO();
            friendRequestDTO.setSenderId( friend.getSender().getId());
            friendRequestDTO.setStatus(String.valueOf(friend.getStatus()));
            friendRequestDTO.setSenderName(sender.get().getName());
            friendRequestsList.add(friendRequestDTO);
        }
        return APIResponse.success("friendRequest list",friendRequestsList);
    }

    public ResponseEntity<?> handleFriendRequest(int receiverId, int requestId, String requestAction) {
        if (receiverId == requestId) {
            return ResponseEntity.badRequest().body("Receiver and sender cannot be the same.");
        }
        UserFriend userFriend = userFriendRepository.findByReceiverIdAndRequestIdAndStatus(receiverId, requestId, String.valueOf(FriendshipStatus.PENDING));
        if (userFriend == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You haven't received any request from the given user.");
        }
        if (requestAction.equalsIgnoreCase("accept")) {
            userFriend.setStatus(FriendshipStatus.ACCEPTED);
            userFriend.setAcceptance_date(LocalDateTime.now());
            userFriendRepository.save(userFriend);
            return ResponseEntity.ok("Friend request accepted successfully.");
        } else if (requestAction.equalsIgnoreCase("reject")) {
            userFriend.setStatus(FriendshipStatus.REJECTED);
            userFriendRepository.save(userFriend);
            return ResponseEntity.ok("Friend request rejected successfully.");
        } else {
            UserFriend requestStatus = userFriendRepository.findBySenderIdAndReceiverId(requestId, receiverId);
            if (requestStatus.getStatus()==FriendshipStatus.PENDING) {
                return ResponseEntity.ok("You both are already friends.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("You haven't received any request from the given user.");
            }
        }
    }


    public ResponseEntity<APIResponse> getFriendsByUserId(int userId) {
        List<Integer> friendsIds = userFriendRepository.findFriendIds(userId);
        if(friendsIds.isEmpty())
        return  APIResponse.error("this user have no friends");
        List<UserProfile> userProfiles=    userProfileRepository.findAllById(friendsIds);
       if (userProfiles.isEmpty()){
           return  APIResponse.error("user profiles are empty ");
       }
        List<FriendResponse> friendResponses = userProfiles.stream()
                .map(userProfile -> new FriendResponse(
                        userProfile.getId(),
                        userProfile.getName(),
                        userProfile.getEmail(),
                        userProfile.getProfilePicPath()
                ))
                .collect(Collectors.toList());

        return  APIResponse.success("friends ",friendResponses);

    }


}





