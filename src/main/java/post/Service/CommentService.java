package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import post.Entities.Comment;
import post.Entities.Post;
import post.Entities.UserFriend;
import post.Repositories.CommentRepository;
import post.Repositories.PostRepository;
import post.Repositories.UserFriendRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserFriendRepository userFriendRepository;

    public ResponseEntity<?> writeACommentForPost(Comment comment) {
        int postId = comment.getPost().getId();
        int userId=comment.getUser().getId();

        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found.");
        }
        Post post = postOptional.get();
        int postOwnerId = post.getUser().getId();

        if (userId == postOwnerId) {
            comment.setCommentedAt(LocalDateTime.now());
            Comment savedComment = commentRepository.save(comment);
            ResponseEntity.ok("you are commented on your own post");

        } else {
            // User is not the owner of the post, check if they are friends
            Optional<UserFriend> friend = Optional.ofNullable(userFriendRepository.findBySenderIdAndReceiverId(postOwnerId, userId));
            if (friend.isEmpty() || !friend.get().getStatus().equalsIgnoreCase("accepted")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't comment on this post.");
            }
        }

        comment.setCommentedAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);

        return ResponseEntity.ok("Comment added successfully.");
    }

}
