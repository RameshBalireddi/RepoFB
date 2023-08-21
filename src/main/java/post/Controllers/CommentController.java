package post.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import post.APIResponse.APIResponse;
import post.DTO.CommentDTO;
import post.Security.GetUser;
import post.Service.CommentService;

@RestController
@RequestMapping("comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping("add")
    public ResponseEntity<APIResponse> writeACommentForPost(@RequestBody @Valid CommentDTO commentDTO) {
        return     commentService.writeACommentForPost(commentDTO);

    }
    @GetMapping("list")
    public ResponseEntity<APIResponse> getComments( @RequestParam(required = false,defaultValue = "0") int postId){
                     return commentService.getCommentsByPostId(postId);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<APIResponse> deleteComment(@PathVariable int commentId) {
        int userId= GetUser.getUserId();
        return commentService.deleteCommentById(commentId, userId);
    }

    @PutMapping("update/{commentId}")
    public ResponseEntity<APIResponse> updateComment(@PathVariable int commentId, @RequestBody String commentText){
        int userId=GetUser.getUserId();
       return commentService.updateCommentById(commentId,userId,commentText);
    }




}
