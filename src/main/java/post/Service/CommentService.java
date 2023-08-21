package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import post.Security.GetUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        int userId = GetUser.getUserId();

        Optional<Post> optionalPost = postRepository.findById(postId);
        Optional<UserProfile> optionalUser = userProfileRepository.findById(userId);

        if (!optionalPost.isPresent() || !optionalUser.isPresent()) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("posts are not found")).getBody();
        }

        Post post = optionalPost.get();
        UserProfile user = optionalUser.get();
        int postOwnerId = post.getUser().getId();

        Optional<UserFriend> friend = Optional.ofNullable(userFriendRepository.findBySenderIdAndReceiverId(postOwnerId, userId));
        if (userId != postOwnerId && (friend.isEmpty() || friend.get().getStatus() != FriendshipStatus.ACCEPTED)) {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error("you cant comment on this post")).getBody();
        }

        Comment comment = new Comment();
        comment.setUser(user);
        comment.setPost(post);
        comment.setComment(commentDTO.getComment());
        comment.setCommentedAt(LocalDateTime.now());
        Comment savedComment = commentRepository.save(comment);

        String successMessage = (userId == postOwnerId) ? "You commented on your own post." : "Comment added successfully.";
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success(successMessage,comment)).getBody();
    }


    public ResponseEntity<APIResponse> getCommentsByPostId(int postId) {
        List<Comment> commentList = (postId == 0)
                ? commentRepository.findAll()
                : commentRepository.findByPostId(postId);

        if (commentList.isEmpty()) {
            return APIResponse.error((postId == 0) ? "No comments found." : "This post has no comments.");
        }
        List<CommentResponse> commentsList = commentList.stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getUser().getId(),
                        comment.getPost().getId(),
                        comment.getComment()))
                .collect(Collectors.toList());

        return APIResponse.success("comments:", commentsList);
    }


    public ResponseEntity<APIResponse> deleteCommentById(int commentId, int userId) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if (optionalComment.isEmpty()) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("comments are not found")).getBody();
        }
        Comment comment = optionalComment.get();
        int commentUserId = comment.getUser().getId();

        if (userId != commentUserId) {
            return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error("you are not eligible to delete this comment")).getBody();
        }
        commentRepository.deleteById(commentId);
        return APIResponse.success("Comment deleted successfully", commentId);
    }

    public ResponseEntity<APIResponse> updateCommentById(int commentId, int userId,String commentText) {
      Optional<Comment> comment=  commentRepository.findById(commentId);

      if(comment.isEmpty()){
          return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("comments are not found")).getBody();
      }
      Comment comment1=   comment.get();
      if(comment1.getUser().getId()!=userId) {
          return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error("this user not allow to update this comment")).getBody();
      }
        comment1.setComment(commentText);
        commentRepository.save(comment1);
       return APIResponse.success("comment updated successfully ",commentText);
    }
}
