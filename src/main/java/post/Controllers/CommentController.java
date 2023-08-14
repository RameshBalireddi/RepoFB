package post.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import post.Entities.Comment;
import post.Service.CommentService;

@RestController
public class CommentController {



    @Autowired
    CommentService commentService;

    @PostMapping("/addComment/")
    public ResponseEntity<?>  writeACommentForPost(@RequestBody @Valid Comment comment){
          return  commentService.writeACommentForPost(comment);
    }

}
