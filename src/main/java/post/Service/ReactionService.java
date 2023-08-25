package post.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import post.APIResponse.APIResponse;

import post.Entities.*;
import post.Enum.FriendshipStatus;
import post.Repositories.*;
import post.Responses.ReactionResponse;
import post.Security.ObjectUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReactionService {

    @Autowired
    PostRepository postRepository;
    @Autowired
    ReactionRepository reactionRepository;
    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    FollowersRepository followersRepository;

    @Autowired
    NotificationRepo notificationRepo;

    public ResponseEntity<APIResponse> reactPost(int reactUserId, int postId, boolean reactionStatus) {
        UserProfile userProfile = userProfileRepository.findById(reactUserId).orElse(null);
        if( userProfile==null)
            return APIResponse.errorNotFound("user not found");
        Reaction existingLike = reactionRepository.findByUserIdAndPostId(reactUserId, postId);

        if (existingLike != null && existingLike.isLiked()) {
            return APIResponse.errorBadRequest("you already react this post");
        }
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null ) {
            return APIResponse.errorNotFound("Posts are not found");
        }
        UserProfile postUser=post.getUser();
        int postUserId = post.getUser().getId();
        if (postUserId != reactUserId) {
//            UserFriend friendStatus = userFriendRepository.findBySenderIdAndReceiverId(postUserId, reactUserId);
            Followers friendStatus=followersRepository.findByUserAndFollowerAndFriendshipStatus(postUserId,reactUserId, String.valueOf(FriendshipStatus.ACCEPTED));
            if (friendStatus == null || friendStatus.getRequestStatus() != FriendshipStatus.ACCEPTED) {
                return APIResponse.errorUnauthorised("You are not follower of this user so you cant react this post");

            }
        }
        Reaction reaction = new Reaction(userProfile,post,reactionStatus,LocalDateTime.now());
        reactionRepository.save(reaction);
        Notification notification=new Notification(postUser,userProfile.getName()+ "  liked your post",LocalDateTime.now());
        notificationRepo.save(notification);

        String successMessage = (reactionStatus == false) ? "post unliked successfully" : "Post liked successfully.";
        return APIResponse.success(successMessage, reaction.isLiked());
    }
    public ResponseEntity<APIResponse> changeReactionForPost(int postId,int userId) {
        Optional<Reaction> reaction = Optional.ofNullable(reactionRepository.findByUserIdAndPostId(userId, postId));

        if(reaction.isEmpty()){
             return APIResponse.errorUnauthorised("before you never react this post so you cant react now");
        }
            Reaction reaction1 = reaction.get();
            Boolean status=reaction1.isLiked();
            if(status){
            reaction1.setLiked(false);
            reactionRepository.save(reaction1);
            return APIResponse.success("post unliked successfully", reaction1.getPost());
             } else
            reaction1.setLiked(true);
            reactionRepository.save(reaction1);
            return APIResponse.success("post like successfully", reaction1.getPost());
            }

    public ResponseEntity<APIResponse> getAllReactions() {

        List<Reaction> reactions= reactionRepository.findAll();
        if(reactions.isEmpty()){
            return  APIResponse.errorNotFound("no one react any post");
        }
        List<ReactionResponse> reactionResponses=reactions.stream()
                .map(r->new ReactionResponse(r.getUser().getId(),r.getPost().getId(),r.getId(),r.isLiked())).collect(Collectors.toList());

        return APIResponse.success("reactions",reactionResponses);
    }

    public ResponseEntity<APIResponse> getAllReactionsById() {

        List<Reaction> reactions= reactionRepository.findByUserId(ObjectUtil.getUserId());
        if(reactions.isEmpty()){
            return  APIResponse.errorNotFound("you are not react any post ");
        }
        List<ReactionResponse> reactionResponses=reactions.stream()
                .map(r->new ReactionResponse(r.getUser().getId(),r.getPost().getId(),r.getId(),r.isLiked())).collect(Collectors.toList());

        return APIResponse.success("reactions",reactionResponses);
    }

}





