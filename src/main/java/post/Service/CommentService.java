package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import post.APIResponse.APIResponse;
import post.DTO.CommentDTO;
import post.DTO.CommentRequest;
import post.Entities.*;
import post.Repositories.*;
import post.Responses.CommentResponse;
import post.Enum.FriendshipStatus;
import post.Security.ObjectUtil;

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
    private FollowersRepository followersRepository;
    @Autowired
    private UserProfileRepository userProfileRepository;


    public ResponseEntity<APIResponse> writeACommentForPost(CommentDTO commentDTO) {
        int postId = commentDTO.getPostId();
        int userId = ObjectUtil.getUserId();

        Optional<Post> optionalPost = postRepository.findById(postId);
        Optional<UserProfile> optionalUser = userProfileRepository.findById(userId);

        if (!optionalPost.isPresent() || !optionalUser.isPresent()) {
            return  APIResponse.errorNotFound("posts are not found");
        }

        Post post = optionalPost.get();
        UserProfile user = optionalUser.get();
        int postOwnerId = post.getUser().getId();
        Optional<Followers> follower = Optional.ofNullable(followersRepository.findByUserAndFollower(postOwnerId, userId));
        if (userId != postOwnerId && (follower.isEmpty() || follower.get().getRequestStatus() != FriendshipStatus.ACCEPTED)) {
            return  APIResponse.errorUnauthorised("you are not allow to reply to this post because you are not follower of this user  ");
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

        Post post=postRepository.findById(postId).orElse(null);
        if(post==null){
            return  APIResponse.errorBadRequest("please enter valid post id");
        }
        Followers followerStatus=followersRepository.findByUserAndFollower(post.getUser().getId(),ObjectUtil.getUserId());

        if(followerStatus==null   || followerStatus.getRequestStatus()!=FriendshipStatus.ACCEPTED  ){
            return  APIResponse.errorUnauthorised("you are not allow to read this post comments");
        }
        List<Comment> commentList =commentRepository.findByPostId(postId);
        if (commentList.isEmpty()) {
            return APIResponse.errorNotFound( "no one  comment on this post");
        }


         if(followerStatus==null   || followerStatus.getRequestStatus()!=FriendshipStatus.ACCEPTED  ){
             return  APIResponse.errorUnauthorised("you are not allow to read this post comments");
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

    public ResponseEntity<APIResponse> getAllComments() {

        List<Comment> commentList =commentRepository.findAll();
        if (commentList.isEmpty()) {
            return APIResponse.errorNotFound( "no one can comment on any post");
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
        Comment comment= commentRepository.findById(commentId).orElse(null);
        if (comment==null) {
            return  APIResponse.errorNotFound("please enter valid comment ID");
        }
        int commentUserId = comment.getUser().getId();
        if (userId != commentUserId) {
            return  APIResponse.errorUnauthorised("you are not eligible to delete this comment");
        }
        commentRepository.deleteById(commentId);
        return APIResponse.success("Comment deleted successfully", commentId);
    }

    public ResponseEntity<APIResponse> updateCommentById(CommentRequest commentRequest) {
      Comment comment=  commentRepository.findById(commentRequest.getCommentId()).orElse(null);

      if(comment==null){
          return  APIResponse.errorBadRequest("please enter valid comment Id");
      }

      if(comment.getUser().getId()!=ObjectUtil.getUserId()) {
          return  APIResponse.errorUnauthorised("you are not write this comment so you can't update");
      }
        comment.setComment(comment.getComment());
        commentRepository.save(comment);
       return APIResponse.success("comment updated successfully ",commentRequest);
    }
}
