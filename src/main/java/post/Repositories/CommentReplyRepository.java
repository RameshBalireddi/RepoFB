package post.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import post.Entities.CommentReply;
@Repository
public interface CommentReplyRepository extends JpaRepository<CommentReply,Integer> {



}
