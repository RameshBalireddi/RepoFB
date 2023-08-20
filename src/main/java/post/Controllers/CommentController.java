package post.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import post.APIResponse.APIResponse;
import post.DTO.CommentDTO;
import post.Entities.Comment;
import post.Security.UserIdContextHolder;
import post.Service.CommentService;

@RestController
@RequestMapping("comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping("add")
    public ResponseEntity<APIResponse> writeACommentForPost(@RequestBody @Valid CommentDTO commentDTO) {
        ResponseEntity<APIResponse> responseEntity = commentService.writeACommentForPost(commentDTO);
        return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
    }
    @GetMapping("list")
    public ResponseEntity<APIResponse> getComments( @RequestParam(required = false,defaultValue = "0") int postId){
                     return commentService.getCommentsByPostId(postId);
    }


    @DeleteMapping("/{commentId}")
    public ResponseEntity<APIResponse> deleteComment(@PathVariable int commentId) {
        int userId= UserIdContextHolder.getUserId();
        return commentService.deleteCommentById(commentId, userId);
    }

    @PutMapping("update/{commentId}")
    public APIResponse updateComment(@PathVariable int commentId,@RequestBody String commentText){
        int userId=UserIdContextHolder.getUserId();
       return commentService.updateCommentById(commentId,userId,commentText);
    }




}
