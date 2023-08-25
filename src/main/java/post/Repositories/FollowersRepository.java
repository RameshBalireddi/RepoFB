package post.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import post.Entities.Followers;
import post.Enum.FriendshipStatus;

import java.util.List;

@Repository
public interface FollowersRepository extends JpaRepository<Followers,Integer> {


    @Query(value = "SELECT * FROM followers WHERE user_id = :receiverId AND follower_id = :senderId AND request_status = :status ORDER BY request_date DESC LIMIT 1", nativeQuery = true)
    Followers findByUserAndFollowerAndFriendshipStatus(int receiverId, int senderId, String status);


    @Query(value = "SELECT * FROM followers WHERE user_id = :receiverId AND follower_id = :senderId ORDER BY request_date DESC LIMIT 1", nativeQuery = true)
    Followers findByUserAndFollower(@Param("receiverId") int receiverId, @Param("senderId") int senderId);

    @Query(value = "SELECT * FROM followers WHERE user_id = :receiverId AND request_status = 'PENDING'", nativeQuery = true)
    List<Followers> findBPendingRequestsForUser(@Param("receiverId") int receiverId);
    @Query(value = "SELECT * FROM followers WHERE user_id = :userId AND request_status = 'ACCEPTED'", nativeQuery = true)
    List<Followers> findByUserIdAndHisFollowersAndStatusAccepted(@Param("userId")int userId);

    @Query(value = "SELECT * FROM followers WHERE follower_id = :userId AND request_status = 'ACCEPTED'", nativeQuery = true)
    List<Followers> findByFollowingIdAndStatusAccepted(@Param("userId")int userId);


    @Query(value = "SELECT follower_id  FROM followers WHERE user_id = :userId AND request_status = 'ACCEPTED'", nativeQuery = true)
    List<Integer> findFollowers(@Param("userId")int userId);

    @Query(value = "SELECT * FROM followers WHERE follower_id = :userId AND request_status = 'ACCEPTED'", nativeQuery = true)
    List<Integer> findFollowingIds(@Param("userId")int userId);


    @Query(value = "SELECT * FROM followers WHERE  request_status = 'PENDING'", nativeQuery = true)
    List<Followers> findByRequestStatusIsPending();
}
