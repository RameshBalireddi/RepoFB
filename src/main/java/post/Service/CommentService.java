package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import post.APIResponse.APIResponse;
import post.DTO.CommentDTO;
import post.Responses.CommentResponse;
import post.Entities.Comment;
import post.Entities.Post;
import post.Entities.UserFriend;
import post.Entities.UserProfile;
import post.Enum.FriendshipStatus;
import post.Repositories.CommentRepository;
import post.Repositories.PostRepository;
import post.Repositories.UserFriendRepository;
import post.Repositories.UserProfileRepository;
import post.Security.UserIdContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserFriendRepository userFriendRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;

    public ResponseEntity<APIResponse> writeACommentForPost(CommentDTO commentDTO) {
        int postId = commentDTO.getPostId();
        int userId = UserIdContextHolder.getUserId();

        Optional<Post> optionalSharePost = postRepository.findById(postId);
        Optional<UserProfile> userProfile = userProfileRepository.findById(userId);

        if (!optionalSharePost.isPresent()) {
            return APIResponse.error("Post not found.");
        }

        if (!userProfile.isPresent()) {
            return APIResponse.error("User not found.");
        }

        Post post = optionalSharePost.get();
        UserProfile user = userProfile.get();
        int postOwnerId = post.getUser().getId();

        if (userId == postOwnerId) {
            Comment comment = new Comment();
            comment.setUser(user);
            comment.setPost(post);
            comment.setComment(commentDTO.getComment());
            comment.setCommentedAt(LocalDateTime.now());
            Comment savedComment = commentRepository.save(comment);
            return APIResponse.success("You commented on your own post.", commentDTO);
        } else {
            Optional<UserFriend> friend = Optional.ofNullable(userFriendRepository.findBySenderIdAndReceiverId(postOwnerId, userId));
            if (friend.isEmpty() || friend.get().getStatus() != FriendshipStatus.ACCEPTED) {
                return APIResponse.error("You can't comment on this post.");
            }
        }

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setComment(commentDTO.getComment());
        comment.setCommentedAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);
        return APIResponse.success("Comment added successfully.", savedComment);
    }

    public ResponseEntity<APIResponse> getCommentsByPostId(int postId) {

      List<Comment> commentList=  commentRepository.findByPostId(postId);
      List<CommentResponse> commentsList=new ArrayList<>();

          for(Comment comment:commentList){
              CommentResponse commentResponse=new CommentResponse();
              commentResponse.setCommentId(comment.getId());
              commentResponse.setUserId(comment.getUser().getId());
              commentResponse.setPostId(comment.getPost().getId());
              commentResponse.setComment(comment.getComment());
              commentsList.add(commentResponse);
          }
      if(commentList.isEmpty()  || commentList==null){
          return  APIResponse.error("this post have no comments");
      }
       return APIResponse.success("comments are :",commentsList);
    }
    public ResponseEntity<APIResponse> deleteCommentById(int commentId, int userId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            return APIResponse.error("Comment not found.");
        }
        Comment comment = optionalComment.get();
        int commentUserId = comment.getUser().getId();

        if (userId != commentUserId) {
            return APIResponse.error("You are not eligible to delete this comment.");
        }
        commentRepository.deleteById(commentId);
        return APIResponse.success("Comment deleted successfully", comment);
    }


}
