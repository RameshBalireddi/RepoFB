package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import post.Entities.Comment;
import post.Entities.Reply;
import post.Entities.UserFriend;
import post.Enum.FriendshipStatus;
import post.Repositories.ReplyRepository;
import post.Repositories.UserFriendRepository;

import java.time.LocalDateTime;

@Service
public class ReplyService {
    @Autowired
    UserFriendRepository userFriendRepository;

    @Autowired
    ReplyRepository replyRepository;

    public ResponseEntity<String> writeReplyForComment(Reply reply) {
        int commentUserId = reply.getComment().getUser().getId();
        int replyUserId = reply.getUser().getId();
        UserFriend friendStatus = userFriendRepository.findBySenderIdAndReceiverId(commentUserId, replyUserId);

        if (friendStatus != null && friendStatus.getStatus() == FriendshipStatus.ACCEPTED){
            reply.setReplyAt(LocalDateTime.now());
            replyRepository.save(reply);
            return ResponseEntity.ok("Reply added successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only reply to comments of friends.");
        }
    }

}
