package post.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import post.APIResponse.APIResponse;

import post.Entities.Reaction;
import post.Entities.Post;
import post.Entities.UserFriend;
import post.Entities.UserProfile;
import post.Enum.FriendshipStatus;
import post.Repositories.ReactionRepository;
import post.Repositories.PostRepository;
import post.Repositories.UserFriendRepository;
import post.Repositories.UserProfileRepository;
import post.Security.GetUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        int postUserId = post.getUser().getId();
        if (postUserId != reactUserId) {
            UserFriend friendStatus = userFriendRepository.findBySenderIdAndReceiverId(postUserId, reactUserId);
            if (friendStatus == null || friendStatus.getStatus() != FriendshipStatus.ACCEPTED) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error("You both are not friends, so you can't like this post")).getBody();

            }
        }

        Reaction reaction = new Reaction();
        reaction.setUser(userProfile);
        reaction.setPost(post);
        reaction.setLiked(reactionStatus);
        reaction.setLikedAt(LocalDateTime.now());
        reactionRepository.save(reaction);

        String successMessage = (postUserId == reactUserId) ? "You liked your own post." : "Post liked successfully.";
        return APIResponse.success(successMessage, reaction);
    }


    public ResponseEntity<APIResponse> changeReactionForPost(int postId,int userId) {
        Optional<Reaction> reaction = Optional.ofNullable(reactionRepository.findByUserIdAndPostId(postId, userId));

        if(reaction.isEmpty()){
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error("You are not react this post")).getBody();
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
        return APIResponse.success("reactions",reactions);
    }
    public ResponseEntity<APIResponse> getAllReactionsById() {

     Optional<Reaction> reactions= reactionRepository.findAllById(GetUser.getUserId());
     if(reactions.isEmpty()){
         return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.error("reactions are not found from given user")).getBody();
     }
     return APIResponse.success("reactions",reactions);
    }
}





