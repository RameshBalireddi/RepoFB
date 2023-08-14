package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import post.Entities.Like;
import post.Entities.Post;
import post.Entities.UserFriend;
import post.Repositories.LikeRepository;
import post.Repositories.PostRepository;
import post.Repositories.UserFriendRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

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
        Optional<Post> post=  postRepository.findById(postId);
       int postUserId=   post.get().getUser().getId();
       System.out.println(postUserId  +"postUserId");
        int likeUserId = like.getUser().getId();
        Like existingLike = likeRepository.findByUserIdAndPostId(likeUserId, postUserId);
        System.out.println(existingLike  +"existingLike");
        if (existingLike != null && existingLike.isLiked()) {
            return ResponseEntity.ok("You have already liked this post.");
        }
        if (postUserId != likeUserId) {
            Optional<UserFriend> friend = Optional.ofNullable(userFriendRepository.findBySenderIdAndReceiverId(postUserId, likeUserId));
            if (friend.isPresent()) {
                String friendStatus = friend.get().getStatus();
                System.out.println(friendStatus  +"friendStatus");
                if (friendStatus.equalsIgnoreCase("accepted")) {
                        like.setLiked(true);
                        like.setLikedAt(LocalDateTime.now());
                        likeRepository.save(like);
                        return ResponseEntity.ok("Post liked successfully.");
                } else {
                    return ResponseEntity.ok("You both are not friends, so you can't like this post.");
                }
            } else {
                return ResponseEntity.ok("You both are not friends, so you can't like this post.");
            }
        } else {
            boolean likeStatus = like.isLiked();
            if (likeStatus) {
                like.setLiked(true);
                like.setLikedAt(LocalDateTime.now());
                likeRepository.save(like);
                return ResponseEntity.ok("You liked your own post.");
            } else {
                return ResponseEntity.ok("You can't unlike your own post.");
            }
        }
    }
}



