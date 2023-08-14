package post.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import post.Entities.Like;

public interface LikeRepository extends JpaRepository<Like,Integer> {
    @Query(value = "SELECT * FROM likes WHERE user_id = :likeUserId AND post_id = :postId", nativeQuery = true)
    Like findByUserIdAndPostId(@Param("likeUserId") int likeUserId, @Param("postId") int postId);

}
