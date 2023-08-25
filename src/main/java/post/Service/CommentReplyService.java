package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import post.APIResponse.APIResponse;
import post.DTO.CommentReplyDTO;
import post.DTO.CommentReplyRequest;
import post.Entities.*;
import post.Enum.FriendshipStatus;
import post.Repositories.CommentReplyRepository;
import post.Repositories.CommentRepository;
import post.Repositories.FollowersRepository;
import post.Repositories.UserProfileRepository;
import post.Responses.CommentReplyResponse;
import post.Security.ObjectUtil;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommentReplyService {
//    @Autowired
//    UserFriendRepository userFriendRepository;

    @Autowired
    FollowersRepository followersRepository;
    @Autowired
    UserProfileRepository userProfileRepository;
     @Autowired
    CommentRepository commentRepository;
    @Autowired
    CommentReplyRepository commentReplyRepository;

    public ResponseEntity<APIResponse> writeReplyForComment(CommentReplyDTO reply) {
        int commentId = reply.getCommentId();
        int replyUserId = ObjectUtil.getUserId();

        Comment comment = commentRepository.findById(commentId).orElse(null);
        UserProfile user = userProfileRepository.findById(replyUserId).orElse(null);

        if (comment==null) {
            return APIResponse.errorBadRequest("Comment not found please enter valid comment id.");
        }

        if (user==null) {
            return APIResponse.errorNotFound("User not found.");
        }

        Followers friendStatus =  followersRepository.findByUserAndFollower(
                comment.getUser().getId(), replyUserId);
        if ( comment.getUser().getId()!=replyUserId && friendStatus == null  || friendStatus.getRequestStatus()!=FriendshipStatus.ACCEPTED) {
            return APIResponse.errorUnauthorised("you are not allow  this  comment reply ");
        }
        CommentReply commentReply = new CommentReply();
        commentReply.setUser(user);
        commentReply.setComment(comment);
        commentReply.setReply(reply.getCommentReply());
        commentReply.setReplyAt(LocalDateTime.now());
        commentReplyRepository.save(commentReply);

        return APIResponse.successCreate("Reply added successfully.", reply);
    }

    public ResponseEntity<APIResponse> getALlCommentReplies(Integer commentId) {
        List<CommentReply> commentReplies = (commentId == 0)
                ? commentReplyRepository.findAll()
                : commentReplyRepository.findByCommentId(commentId);

        if (commentReplies.isEmpty()) {
            return APIResponse.errorNotFound((commentId == 0) ? "Replies are not found." : "This comment has no replies.");
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

         CommentReply commentReply=  commentReplyRepository.findById(replyId).orElse(null);
         if(commentReply==null){
             return APIResponse.errorNotFound("comment reply are not found");}
         int userId= ObjectUtil.getUserId();
          UserProfile userProfile= userProfileRepository.findById(userId).orElse(null);
         if(userProfile==null){
             return APIResponse.errorNotFound("user not found");}
         if(commentReply.getUser().getId()!=userId){
             return APIResponse.errorUnauthorised("you are not allow to delete this  comment reply ");
         }
         commentReplyRepository.deleteById(replyId);
         return APIResponse.success("comment reply deleted successfully ",commentReply.getReply());
    }

    public ResponseEntity<APIResponse> editReplyByReplyId(CommentReplyRequest commentReplyRequest) {

     CommentReply commentReply=   commentReplyRepository.findById(commentReplyRequest.getCommentReplyId()).orElse(null);
      if(commentReply==null){
          return APIResponse.errorBadRequest(" comment reply not found enter valid reply Id");
      }
      if( commentReply.getUser().getId()!= ObjectUtil.getUserId()){
          return APIResponse.errorUnauthorised(" you are not allow to edit this reply for  comment");
      }
       commentReply.setReply(commentReplyRequest.getCommentReplyText());
      commentReplyRepository.save(commentReply);
      return APIResponse.success("reply updated successfully ",commentReplyRequest);

    }
}





