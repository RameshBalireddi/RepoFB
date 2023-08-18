package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
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
import post.Security.UserIdContextHolder;

import java.time.LocalDateTime;
import java.util.Optional;

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

        Optional<Comment> commentOptional = commentRepository.findById(commentId);
        Optional<UserProfile> userProfileOptional = userProfileRepository.findById(replyUserId);

        if (commentOptional.isEmpty() ) {
            return APIResponse.error("Comment  not found.");
        }
        if( userProfileOptional.isEmpty())
            return APIResponse.error("user  not found.");

        Comment comment = commentOptional.get();
        UserProfile userProfile = userProfileOptional.get();

        UserFriend friendStatus = userFriendRepository.findBySenderIdAndReceiverId(
                comment.getUser().getId(), userProfile.getId());

        if (friendStatus != null && friendStatus.getStatus() == FriendshipStatus.ACCEPTED) {
            CommentReply commentReply = new CommentReply();
            commentReply.setUser(userProfile);
            commentReply.setComment(comment);
            commentReply.setReply(reply.getCommentReply());
            commentReply.setReplyAt(LocalDateTime.now());
            commentReplyRepository.save(commentReply);
            return APIResponse.success("Reply added successfully.", reply);
        } else {
            return APIResponse.error("You can only reply to comments of friends.");
        }
    }




}
