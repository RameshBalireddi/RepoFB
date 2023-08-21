package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import post.APIResponse.APIResponse;
import post.DTO.CommentReplyDTO;
import post.Entities.Comment;
import post.Entities.CommentReply;
import post.Entities.UserFriend;
import post.Entities.UserProfile;
import post.Enum.FriendshipStatus;
import post.Repositories.CommentReplyRepository;
import post.Repositories.CommentRepository;
import post.Repositories.UserFriendRepository;
import post.Repositories.UserProfileRepository;
import post.Responses.CommentReplyResponse;
import post.Security.GetUser;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentReplyService {
    @Autowired
    UserFriendRepository userFriendRepository;

    @Autowired
    UserProfileRepository userProfileRepository;
     @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentReplyRepository commentReplyRepository;

    public ResponseEntity<APIResponse> writeReplyForComment(CommentReplyDTO reply) {
        int commentId = reply.getCommentId();
        int replyUserId = GetUser.getUserId();

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findById(replyUserId);

        if (optionalComment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("Comment not found.").getBody());
        }

        if (optionalUserProfile.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("User not found.").getBody());
        }

        Comment comment = optionalComment.get();
        UserProfile user = optionalUserProfile.get();

        UserFriend friendStatus = userFriendRepository.findByReceiverIdAndRequestIdAndStatus(
                comment.getUser().getId(), replyUserId, String.valueOf(FriendshipStatus.ACCEPTED));
        if (friendStatus == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error("you are not allow to delete this  comment reply ")).getBody();
        }

        CommentReply commentReply = new CommentReply();
        commentReply.setUser(user);
        commentReply.setComment(comment);
        commentReply.setReply(reply.getCommentReply());
        commentReply.setReplyAt(LocalDateTime.now());
        commentReplyRepository.save(commentReply);

        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("Reply added successfully.", reply)).getBody();
    }

    public ResponseEntity<APIResponse> getALlCommentReplies(Integer commentId) {
        List<CommentReply> commentReplies = (commentId == 0)
                ? commentReplyRepository.findAll()
                : commentReplyRepository.findByCommentId(commentId);

        if (commentReplies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error((commentId == 0) ? "Replies are not found." : "This comment has no replies.")).getBody();
        }

        List<CommentReplyResponse> commentReplyResponseList = commentReplies.stream()
                .map(commentReply -> new CommentReplyResponse(
                        commentReply.getComment().getId(),
                        commentReply.getUser().getId(),
                        commentReply.getReply(),
                        commentReply.getId(),
                        commentReply.getReplyAt()))
                .collect(Collectors.toList());

        return APIResponse.success("commentReplies:", commentReplyResponseList);
    }


    public ResponseEntity<APIResponse> deleteReplyById(int replyId) {

         Optional<CommentReply> commentReply=  commentReplyRepository.findById(replyId);
         if(commentReply.isEmpty()){
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("comment reply are not found")).getBody();}
         int userId=GetUser.getUserId();
          Optional<UserProfile> userProfile= userProfileRepository.findById(userId);
         if(userProfile.isEmpty()){
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("user not found")).getBody();}
         if(commentReply.get().getId()!=userId){
             return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error("you are not allow to delete this  comment reply ")).getBody();
         }
         commentReplyRepository.deleteById(replyId);
         return APIResponse.success("comment reply deleted successfully ",commentReply.get().getReply());
    }

    public ResponseEntity<APIResponse> editReplyByReplyId(int replyId,String replyText) {
        if(replyText==null){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(APIResponse.error("add some text to reply  ")).getBody();
        }
     CommentReply commentReply=   commentReplyRepository.findById(replyId).orElse(null);
      if(commentReply==null){
          return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("comment reply not found")).getBody();
      }
      if( commentReply.getUser().getId()!= GetUser.getUserId()){
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error(" you are not allow to edit this comment")).getBody();
      }
       commentReply.setReply(replyText);
      commentReplyRepository.save(commentReply);
      return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("reply updated successfully ",replyText)).getBody();

    }
}





