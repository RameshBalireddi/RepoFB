package post.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import post.Entities.Reaction;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction,Integer> {

    @Query(value = "SELECT * FROM likes WHERE user_id = :likeUserId AND post_id = :postId", nativeQuery = true)
    Reaction findByUserIdAndPostId(@Param("likeUserId") int likeUserId, @Param("postId") int postId);

    @Query(value = "SELECT * FROM likes WHERE user_id = :likeUserId ", nativeQuery = true)
    Optional<Reaction> findAllById(@Param("likeUserId")int userId);

    @Query(value = "SELECT * FROM likes WHERE user_id = :userId",nativeQuery = true)
    List<Reaction> findByUserId(@Param("userId") int userId);
}
