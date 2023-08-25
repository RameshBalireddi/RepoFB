package post.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import post.Entities.Comment;
import post.Entities.CommentReply;

import java.util.List;

@Repository
public interface CommentReplyRepository extends JpaRepository<CommentReply,Integer> {

    @Query(value = "select * from replies where comment_id = :commentId", nativeQuery = true)
    List<CommentReply> findByCommentId(@Param("commentId") int commentId);


}
