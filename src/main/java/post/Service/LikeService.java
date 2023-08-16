package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import post.Entities.Like;
import post.Entities.Post;
import post.Entities.UserFriend;
import post.Repositories.LikeRepository;
import post.Repositories.PostRepository;
import post.Repositories.UserFriendRepository;
import java.time.LocalDateTime;

@Service
public class LikeService {

    @Autowired
    UserFriendRepository userFriendRepository;

    @Autowired
    PostRepository postRepository;
    @Autowired
    LikeRepository likeRepository;

    public ResponseEntity<String> likeAPost(Like like) {
        int postId = like.getPost().getId();
        int likeUserId = like.getUser().getId();

        Like existingLike = likeRepository.findByUserIdAndPostId(likeUserId, postId);
        if (existingLike != null && existingLike.isLiked()) {
            return ResponseEntity.ok("You have already liked this post.");
        }
        Post post = postRepository.findById(postId).orElse(null);
        if (post == null) {
            return ResponseEntity.ok("Post not found.");
        }
        int postUserId = post.getUser().getId();

        if (postUserId != likeUserId) {
            UserFriend friendStatus = userFriendRepository.findBySenderIdAndReceiverId(postUserId, likeUserId);
            if (friendStatus == null || !friendStatus.getStatus().equals("ACCEPTED")) {
                return ResponseEntity.ok("You both are not friends, so you can't like this post.");
            }

        }
        boolean likeStatus = like.isLiked();
        like.setLiked(likeStatus);
        like.setLikedAt(LocalDateTime.now());
        likeRepository.save(like);

        if (postUserId == likeUserId) {
            return ResponseEntity.ok(likeStatus ? "You liked your own post." : "You can't unlike your own post.");
        } else {
            return ResponseEntity.ok("Post liked successfully.");
        }
    }

}



