package post.Service;


import org.springframework.beans.factory.annotation.Autowired;
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

import java.time.LocalDateTime;
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

    public ResponseEntity<APIResponse> reactPost(int reactUserId,int postId,boolean reactionStatus) {


        int likeUserId = reactUserId;

        Reaction existingLike = reactionRepository.findByUserIdAndPostId(likeUserId, postId);
        if (existingLike != null && existingLike.isLiked()) {
            return APIResponse.error("you already reacted this post");
        }
        UserProfile userProfile = userProfileRepository.findById(likeUserId).orElse(null);
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null  || userProfile==null) {
            return APIResponse.error("Post not found. and user not found");
        }
        int postUserId = post.getUser().getId();

        if (postUserId != likeUserId) {
            UserFriend friendStatus = userFriendRepository.findBySenderIdAndReceiverId(postUserId, likeUserId);
            if (friendStatus == null || friendStatus.getStatus()!= FriendshipStatus.ACCEPTED) {
                return APIResponse.error("You both are not friends, so you can't like this post.");
            }
        }
        UserProfile senderProfile = userProfileRepository.findById(likeUserId).orElse(null);

        Reaction reaction=new Reaction();
        reaction.setUser(userProfile);
        reaction.setPost(post);
        reaction.setLiked(reactionStatus);
        reaction.setLikedAt(LocalDateTime.now());
        reactionRepository.save(reaction);

        if (postUserId == likeUserId) {
            APIResponse.success("you liked your own post",reaction);
        }
            return APIResponse.success("Post liked successfully.",reaction);
        }

    public APIResponse changeReactionForPost(int postId,int userId) {
        Optional<Reaction> reaction = Optional.ofNullable(reactionRepository.findByUserIdAndPostId(postId, userId));

        if (reaction.isPresent()) {
            Reaction reaction1 = reaction.get();
            Boolean status=reaction1.isLiked();
            if(status){
            reaction1.setLiked(false);
            reactionRepository.save(reaction1);
            return APIResponse.success("post unliked successfully", reaction1.getPost()).getBody();
        } else
            reaction1.setLiked(true);
            reactionRepository.save(reaction1);
            return APIResponse.success("post like successfully", reaction1.getPost()).getBody();
        }
        return APIResponse.error("post are not found").getBody();
    }

}





