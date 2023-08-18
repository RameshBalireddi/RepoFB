package post.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import post.Entities.Comment;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {
    @Query(value = "select * from comments where post_id = :postId", nativeQuery = true)
    List<Comment> findByPostId(@Param("postId") int postId);

}
