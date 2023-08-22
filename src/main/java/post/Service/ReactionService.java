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
import post.Security.GetUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReactionService {

    @Autowired
    UserFriendRepository userFriendRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    ReactionRepository reactionRepository;
    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    NotificationRepo notificationRepo;

    public ResponseEntity<APIResponse> reactPost(int reactUserId, int postId, boolean reactionStatus) {
        UserProfile userProfile = userProfileRepository.findById(reactUserId).orElse(null);
        if(userProfile.isActive()==false || userProfile==null)
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("user not found")).getBody();
        Reaction existingLike = reactionRepository.findByUserIdAndPostId(reactUserId, postId);

        if (existingLike != null && existingLike.isLiked()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error("you already react this post")).getBody();
        }
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("Posts are not found")).getBody();
        }
        UserProfile postUser=post.getUser();
        int postUserId = post.getUser().getId();
        if (postUserId != reactUserId) {
            UserFriend friendStatus = userFriendRepository.findBySenderIdAndReceiverId(postUserId, reactUserId);
            if (friendStatus == null || friendStatus.getStatus() != FriendshipStatus.ACCEPTED) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error("You both are not friends, so you can't like this post")).getBody();

            }
        }
        Reaction reaction = new Reaction(userProfile,post,reactionStatus,LocalDateTime.now());
        reactionRepository.save(reaction);
        Notification notification=new Notification(postUser,userProfile.getName()+ "  liked your post",LocalDateTime.now());
        notificationRepo.save(notification);

        String successMessage = (postUserId == reactUserId) ? "You liked your own post." : "Post liked successfully.";
        return APIResponse.success(successMessage, reaction);
    }


    public ResponseEntity<APIResponse> changeReactionForPost(int postId,int userId) {
        Optional<Reaction> reaction = Optional.ofNullable(reactionRepository.findByUserIdAndPostId(userId, postId));

        if(reaction.isEmpty()){
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("reaction not found")).getBody();
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
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("reactions are not found")).getBody();
        }
        List<ReactionResponse> reactionResponses=reactions.stream()
                .map(r->new ReactionResponse(r.getUser().getId(),r.getPost().getId(),r.getId(),r.isLiked())).collect(Collectors.toList());

        return APIResponse.success("reactions",reactionResponses);
    }

    public ResponseEntity<APIResponse> getAllReactionsById() {

        List<Reaction> reactions= (List<Reaction>) reactionRepository.findByUserId(GetUser.getUserId());
        if(reactions.isEmpty()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("reactions are not found")).getBody();
        }
        List<ReactionResponse> reactionResponses=reactions.stream()
                .map(r->new ReactionResponse(r.getUser().getId(),r.getPost().getId(),r.getId(),r.isLiked())).collect(Collectors.toList());

        return APIResponse.success("reactions",reactionResponses);
    }

    }





