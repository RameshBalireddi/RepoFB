package post.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import post.APIResponse.APIResponse;
import post.DTO.CommentDTO;
import post.DTO.CommentRequest;
import post.Security.ObjectUtil;
import post.Service.CommentService;

@RestController
//@RequestMapping("comment")
public class CommentController {
    @Autowired
    CommentService commentService;

    @PostMapping("comment/add")
    public ResponseEntity<APIResponse> writeACommentForPost(@RequestBody @Valid CommentDTO commentDTO) {
        return     commentService.writeACommentForPost(commentDTO);

    }
    @GetMapping("comments/{postId}")
    public ResponseEntity<APIResponse> getCommentsOnPost(@PathVariable int postId){
                     return commentService.getCommentsByPostId(postId);
    }
    @GetMapping("/comments/list")
    public ResponseEntity<APIResponse> getAllComments(){
        return commentService.getAllComments();
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<APIResponse> deleteComment(@PathVariable int commentId) {
        int userId= ObjectUtil.getUserId();
        return commentService.deleteCommentById(commentId, userId);
    }

    @PutMapping("comment/update/{commentId}")
    public ResponseEntity<APIResponse> updateComment(@PathVariable int commentId, @RequestBody @Valid  CommentRequest commentRequest){
        int userId= ObjectUtil.getUserId();
       return commentService.updateCommentById(commentRequest);
    }

}
