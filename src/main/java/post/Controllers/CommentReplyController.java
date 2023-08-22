package post.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/replies")
    public  ResponseEntity<APIResponse> getAllCommentReplies(@RequestParam(required = false,defaultValue = "0") int commentId){
              return  replyService.getALlCommentReplies(commentId);
    }

      @DeleteMapping("/{replyId}")
        public ResponseEntity<APIResponse> deleteReplyById(@PathVariable int replyId){
             return  replyService.deleteReplyById(replyId);
       }

       @PutMapping("/editCommentReply/{replyId}")
       public ResponseEntity<APIResponse> editReplyByReplyId(@PathVariable int replyId, @RequestBody String commentReply){
        return  replyService.editReplyByReplyId(replyId,commentReply);
       }




   }



