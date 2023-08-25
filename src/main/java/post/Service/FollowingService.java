package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import post.APIResponse.APIResponse;
import post.Entities.Followers;
import post.Entities.UserProfile;
import post.Enum.FriendshipStatus;
import post.Repositories.FollowersRepository;
import post.Repositories.UserProfileRepository;
import post.Responses.FollowersResponse;
import post.Responses.FollowingResponse;
import post.Responses.PendingRequest;
import post.Security.ObjectUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowingService {


    @Autowired
    FollowersRepository followersRepository;
    @Autowired
    UserProfileRepository userProfileRepository;


    public ResponseEntity<APIResponse> sentRequest(int requestId) {
        int senderId = ObjectUtil.getUserId();
        UserProfile sender = userProfileRepository.findById(senderId).orElse(null);
        UserProfile receiver = userProfileRepository.findById(requestId).orElse(null);

        if(senderId==requestId){
            return  APIResponse.errorBadRequest("sender and receiver both are not same ");
        }

        if (sender == null || receiver == null) {
            return
                   APIResponse.errorNotFound("Receiver not found. Please provide a valid request.");
        }

        Followers followStatus = followersRepository.findByUserAndFollowerAndFriendshipStatus(receiver.getId(), sender.getId(), String.valueOf(FriendshipStatus.PENDING));

        if (followStatus == null) {
            Followers followers = new Followers(receiver, sender, FriendshipStatus.PENDING, LocalDateTime.now());
            followersRepository.save(followers);
            return ResponseEntity.ok(APIResponse.success("Follow request sent successfully.",followers.getUser().getId()).getBody());
        } else {
            return APIResponse.errorBadRequest("Follow request already sent.");
        }
    }
    public ResponseEntity<APIResponse> acceptRequest(int requestId) {

        int receiverId = ObjectUtil.getUserId();
        UserProfile receiver = userProfileRepository.findById(receiverId).orElse(null);
        UserProfile sender = userProfileRepository.findById(requestId).orElse(null);

        if(receiverId==requestId){
          return   APIResponse.errorBadRequest("you are not follow your own ");
        }

        if (sender == null || receiver == null) {
            return
                    APIResponse.errorNotFound("Sender not found. Please provide a valid request.");
        }

        // Check if the sender has a pending follow request from the receiver
//        Followers followStatus = followersRepository.findByUserAndFollowerAndFriendshipStatus(receiver.getId(),sender.getId(), String.valueOf(FriendshipStatus.PENDING));
        Followers followStatus = followersRepository.findByUserAndFollower(receiver.getId(),sender.getId());

        if (followStatus == null ) {
            return APIResponse.errorNotFound("Follow request not found");
        }
        if (followStatus.getRequestStatus().equals(FriendshipStatus.ACCEPTED)) {

           return APIResponse.errorBadRequest("You have already accepted that request.");
        }
            followStatus.setRequestStatus(FriendshipStatus.ACCEPTED);
            followStatus.setAcceptDate(LocalDateTime.now());
            followersRepository.save(followStatus);
            return APIResponse.success("Follow request accepted successfully.",requestId);
        }


    public ResponseEntity<APIResponse> rejectRequest(int requestId) {

        int receiverId = ObjectUtil.getUserId();
        UserProfile receiver = userProfileRepository.findById(receiverId).orElse(null);
        UserProfile sender = userProfileRepository.findById(requestId).orElse(null);
        if(receiverId==requestId){
            return  APIResponse.errorBadRequest("own rejection not possible ");
        }
        if (sender == null || receiver == null) {
            return
                   APIResponse.errorBadRequest("Sender not found. Please provide a valid request.");
        }
         Followers followStatus = followersRepository.findByUserAndFollower(receiver.getId(),sender.getId());

        if (followStatus == null ) {
            return APIResponse.errorBadRequest("Follow request not found");
        }
        if (followStatus.getRequestStatus().equals(FriendshipStatus.ACCEPTED)) {
            return
                    APIResponse.errorBadRequest("You cant reject this request because you are already accepted this request");
        }
        if(followStatus.getRequestStatus().equals(FriendshipStatus.REJECTED)){
            return APIResponse.errorBadRequest("you already rejected follow request");
        }

        followStatus.setRequestStatus(FriendshipStatus.REJECTED);
        followersRepository.save(followStatus);
        return ResponseEntity.ok(APIResponse.success("Follow request rejected successfully.",requestId).getBody());
    }


      public  ResponseEntity<APIResponse> getALlFollowers(){

           int userId= ObjectUtil.getUserId();

          List<Followers> followers=followersRepository.findByUserIdAndHisFollowersAndStatusAccepted(userId);

          if (followers.isEmpty()){

              return  APIResponse.errorNotFound("no one follows you");
          }

          List<FollowersResponse> followersList=followers.stream().map(f->new FollowersResponse(userId,f.getFollower().getId())).collect(Collectors.toList());
          return  ResponseEntity.ok(APIResponse.success(" your followers : ",followersList)).getBody();
        }


    public  ResponseEntity<APIResponse> getALlFollowing(){

        int userId= ObjectUtil.getUserId();

        List<Followers> following=followersRepository.findByFollowingIdAndStatusAccepted(userId);
        if (following.isEmpty()){

            return  APIResponse.errorNotFound("you couldn't follow any one ");
        }
        List<FollowingResponse> followingList=following.stream().map(f->new FollowingResponse(userId,f.getUser().getId())).collect(Collectors.toList());
        return  ResponseEntity.ok(APIResponse.success("you following : ",followingList)).getBody();
    }

        public ResponseEntity<APIResponse> pendingRequests(int receiverId) {
        String pending = "PENDING";
        List<Followers> pendingRequests=followersRepository.findBPendingRequestsForUser(receiverId);

        if (pendingRequests.isEmpty()) {
            return APIResponse.errorNotFound("you don't have any pending requests");
        }
        List<PendingRequest> pendingRequestList = pendingRequests.stream()
                .map(p -> new PendingRequest(
                        p.getFollower().getId(),
                        String.valueOf(p.getRequestStatus()),
                        p.getFollower().getName(),p.getUser().getId()))
                        .collect(Collectors.toList());

        return ResponseEntity.ok(APIResponse.success("pending requests", pendingRequestList).getBody());
    }

       public ResponseEntity<APIResponse> AllPendingRequests() {
        String pending = "PENDING";
        List<Followers> pendingRequests;
        String errorMessage;

        pendingRequests = followersRepository.findByRequestStatusIsPending();
        if (pendingRequests.isEmpty()) {
            return APIResponse.errorNotFound("currently you don't have any pending requests");
        }
        List<PendingRequest> pendingRequestList = pendingRequests.stream()
                .map(p -> new PendingRequest(
                        p.getFollower().getId(),
                        String.valueOf(p.getRequestStatus()),
                        p.getFollower().getName(),p.getUser().getId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(APIResponse.success("pending requests", pendingRequestList).getBody());
    }

}

