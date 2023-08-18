package post.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import post.APIResponse.APIResponse;
import post.DTO.CommentReplyDTO;
import post.Entities.CommentReply;
import post.Entities.Reaction;
import post.Service.CommentReplyService;
import post.Service.ReactionService;

@RestController
public class CommentReplyController {

    @Autowired
    CommentReplyService replyService;

    @PostMapping("/reply")
    public ResponseEntity<APIResponse> writeReplyForComment(@RequestBody @Valid CommentReplyDTO replyDTO) {

       return replyService.writeReplyForComment(replyDTO);

    }




}
