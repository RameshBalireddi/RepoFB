package post.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import post.Entities.Comment;
import post.Service.CommentService;

@RestController
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("/comment")
    public ResponseEntity<?> writeACommentForPost(@RequestBody @Valid Comment comment) {
        try {
            ResponseEntity<String> response = (ResponseEntity<String>) commentService.writeACommentForPost(comment);
            return ResponseEntity.ok(response.getBody());
        } catch (NullPointerException n) {
            throw new NullPointerException("Response not found: " + n.getMessage());
        }
    }


    @GetMapping("comments/{postId}")
    public ResponseEntity<?> getComments( @PathVariable int postId){
        try {
            ResponseEntity<?> result = commentService.getCommentsByPostId(postId);
            return ResponseEntity.ok(result.getBody());
        }catch (NullPointerException n){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("comments are not found this post "+ n.getMessage());
        }

    }

    @DeleteMapping("comment/{commentId}/{userId}")
    public ResponseEntity<?> deleteComment(@PathVariable int commentId, @PathVariable int userId) {
        try {
            ResponseEntity<?> result = commentService.deleteCommentById(commentId, userId);
            return ResponseEntity.ok(result.getBody());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error deleting comment: " + e.getMessage());
        }
    }

}
