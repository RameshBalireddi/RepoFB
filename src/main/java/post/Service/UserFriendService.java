package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import post.APIResponse.APIResponse;
import post.Entities.Notification;
import post.Repositories.NotificationRepo;
import post.Responses.FriendResponse;
import post.Entities.UserFriend;
import post.Entities.UserProfile;
import post.Enum.FriendshipStatus;
import post.Repositories.UserFriendRepository;
import post.Repositories.UserProfileRepository;
import post.Responses.PendingRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserFriendService {

    @Autowired
    private UserFriendRepository userFriendRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    NotificationRepo notificationRepo;

    public ResponseEntity<APIResponse> sendFriendRequest(int senderId, int requestId) {
        if (senderId == requestId) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(APIResponse.error("Sender and receiver cannot be the same").getBody());
        }

        UserProfile senderProfile = userProfileRepository.findById(senderId).orElse(null);
        UserProfile receiverProfile = userProfileRepository.findById(requestId).orElse(null);
        if (senderProfile == null || receiverProfile == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(APIResponse.error("Invalid sender ID or receiver ID").getBody());
        }

        UserFriend existingFriendship = userFriendRepository.findBySenderIdAndReceiverId(senderId, requestId);

        if (existingFriendship == null) {
            UserFriend userFriend = new UserFriend();
            userFriend.setSender(senderProfile);
            userFriend.setReceiver(receiverProfile);
            userFriend.setRequest_date(LocalDateTime.now());
            userFriend.setStatus(FriendshipStatus.PENDING);
            userFriendRepository.save(userFriend);
            createFriendRequestNotification(receiverProfile, senderId);

            return APIResponse.success("Friend request sent successfully.", requestId);
        }

        if (existingFriendship.getStatus() == FriendshipStatus.ACCEPTED) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(APIResponse.error("You both are already friends").getBody());
        } else if (existingFriendship.getStatus() == FriendshipStatus.REJECTED) {
            existingFriendship.setStatus(FriendshipStatus.PENDING);
            existingFriendship.setRequest_date(LocalDateTime.now());
            userFriendRepository.save(existingFriendship);
            createFriendRequestNotification(receiverProfile, senderId);

            return APIResponse.success("Your friend request was sent again successfully", requestId);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(APIResponse.error("Friend request was already sent").getBody());
    }

    private void createFriendRequestNotification(UserProfile receiverProfile, int senderId) {
        Notification notification = new Notification(receiverProfile,"you have received a friend request from this user ID: " + senderId,LocalDateTime.now());
         notificationRepo.save(notification);
    }

    public ResponseEntity<APIResponse> getPendingRequests(int receiverId, boolean all) {
        String pending = "PENDING";

        List<UserFriend> pendingRequests;
        String errorMessage;
        if (all) {
            pendingRequests = userFriendRepository.findByStatus(pending);
            errorMessage = "pending requests are not found";
        } else {
            pendingRequests = userFriendRepository.findPendingRequestsByReceiverAndStatus(receiverId, pending);
            errorMessage = "you don't have any pending requests";
        }
        if (pendingRequests.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error(errorMessage).getBody());
        }
        List<PendingRequest> pendingRequestList = pendingRequests.stream()
                .map(p -> new PendingRequest(
                        p.getSender().getId(),
                        String.valueOf(p.getStatus()),
                        p.getSender().getName(),p.getReceiver().getId()))
                        .collect(Collectors.toList());

        return ResponseEntity.ok(APIResponse.success("pending requests", pendingRequestList).getBody());
    }


    public ResponseEntity<APIResponse> handleFriendRequest(int receiverId, int requestId, String requestAction) {
        if (receiverId == requestId) {
            return ResponseEntity.badRequest().body(APIResponse.error("Sender and receiver can't be the same").getBody());
        }

        UserFriend userFriend = userFriendRepository.findByReceiverIdAndRequestIdAndStatus(receiverId, requestId, String.valueOf(FriendshipStatus.PENDING));
        if (userFriend == null) {
            return ResponseEntity.badRequest().body(APIResponse.error("You haven't received any request from the given user").getBody());
        }
        switch (requestAction.toLowerCase()) {
            case "accept":
                userFriend.setStatus(FriendshipStatus.ACCEPTED);
                userFriend.setAcceptance_date(LocalDateTime.now());
                break;
            case "reject":
                userFriend.setStatus(FriendshipStatus.REJECTED);
                break;
            default:
                return ResponseEntity.badRequest().body(APIResponse.error("Invalid request action").getBody());
        }
        if(requestAction.equalsIgnoreCase("accept")){
           UserProfile senderProfile=  userProfileRepository.findById(requestId).orElse(null);
            Notification notification = new Notification(senderProfile,"Your friend request accepted  from user ID: " + receiverId,LocalDateTime.now());
            notificationRepo.save(notification);
        }
        userFriendRepository.save(userFriend);
        return APIResponse.success("Friend request " + requestAction.toLowerCase() + "ed successfully.", requestId);
    }


    public ResponseEntity<APIResponse> getFriendsByUserId(int userId) {
        List<Integer> friendsIds = userFriendRepository.findFriendIds(userId);
        if(friendsIds.isEmpty())
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("this user have no friends")).getBody();
        List<UserProfile> userProfiles=    userProfileRepository.findAllById(friendsIds);
       if (userProfiles.isEmpty()){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("user profiles are empty ")).getBody();
       }
        List<FriendResponse> friendResponses = userProfiles.stream()
                .map(userProfile -> new FriendResponse(
                        userProfile.getId(),
                        userProfile.getName(),
                        userProfile.getEmail(),
                        userProfile.getProfilePicPath()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(APIResponse.success("friends ",friendResponses)).getBody();

    }

}





