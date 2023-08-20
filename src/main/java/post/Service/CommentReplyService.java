package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
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
import post.Security.UserIdContextHolder;
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
        int replyUserId = UserIdContextHolder.getUserId();

        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findById(replyUserId);

        if (optionalComment.isEmpty()) {
            return APIResponse.error("Comment not found.");
        }

        if (optionalUserProfile.isEmpty()) {
            return APIResponse.error("User not found.");
        }

        Comment comment = optionalComment.get();
        UserProfile user = optionalUserProfile.get();

        UserFriend friendStatus = userFriendRepository.findByReceiverIdAndRequestIdAndStatus(
                comment.getUser().getId(), replyUserId, String.valueOf(FriendshipStatus.ACCEPTED));
        if (friendStatus == null) {
            return APIResponse.error("You are not allowed to reply to this comment.");
        }

        CommentReply commentReply = new CommentReply();
        commentReply.setUser(user);
        commentReply.setComment(comment);
        commentReply.setReply(reply.getCommentReply());
        commentReply.setReplyAt(LocalDateTime.now());
        commentReplyRepository.save(commentReply);

        return APIResponse.success("Reply added successfully.", reply);
    }

    public APIResponse getALlCommentReplies(Integer commentId) {
        List<CommentReply> commentReplies = (commentId == 0)
                ? commentReplyRepository.findAll()
                : commentReplyRepository.findByCommentId(commentId);

        if (commentReplies.isEmpty()) {
            return APIResponse.error((commentId == 0) ? "Replies are not found." : "This comment has no replies.").getBody();
        }

        List<CommentReplyResponse> commentReplyResponseList = commentReplies.stream()
                .map(commentReply -> new CommentReplyResponse(
                        commentReply.getComment().getId(),
                        commentReply.getUser().getId(),
                        commentReply.getReply(),
                        commentReply.getId(),
                        commentReply.getReplyAt()))
                .collect(Collectors.toList());

        return APIResponse.success("commentReplies:", commentReplyResponseList).getBody();
    }


    public APIResponse deleteReplyById(int replyId) {

         Optional<CommentReply> commentReply=  commentReplyRepository.findById(replyId);
         if(commentReply.isEmpty()){
             return APIResponse.error("comment reply are not found").getBody();}
         int userId=UserIdContextHolder.getUserId();
          Optional<UserProfile> userProfile= userProfileRepository.findById(userId);
         if(userProfile.isEmpty()){
             return APIResponse.error("user not found").getBody();}
         if(commentReply.get().getId()!=userId){
             return APIResponse.error("you are not allow to delete this  comment reply ").getBody();
         }
         commentReplyRepository.deleteById(replyId);
         return APIResponse.success("comment reply deleted successfully ",commentReply.get().getReply()).getBody();
    }
}





