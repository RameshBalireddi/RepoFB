package post.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import post.Entities.Reply;
import post.Service.ReplyService;

@RestController
public class ReplyController {

    @Autowired
    ReplyService replyService;

    @PostMapping("/reply")
    public ResponseEntity<String> writeReplyForComment(@RequestBody @Valid Reply reply) {
        try {
            ResponseEntity<String> replyResponse = replyService.writeReplyForComment(reply);
            if (replyResponse == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reply not found");
            }
            return ResponseEntity.ok(replyResponse.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while writing the reply: " + e.getMessage());
        }
    }




}
