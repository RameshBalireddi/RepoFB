package post.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import post.Entities.Comment;

public interface CommentRepository extends JpaRepository<Comment,Integer> {


}
