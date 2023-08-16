package post.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import post.Entities.User;
import post.Entities.UserFriend;
import post.Enum.FriendshipStatus;

import java.util.List;
import java.util.Optional;

public interface UserFriendRepository extends JpaRepository<UserFriend,Integer> {

    @Query(value = "SELECT * FROM friendship " +
            "WHERE (user_id_sender = :senderId AND user_id_receiver = :receiverId) " +
            "   OR (user_id_sender = :receiverId AND user_id_receiver = :senderId) " +
            "ORDER BY id DESC " +
            "LIMIT 1", nativeQuery = true)
    UserFriend findBySenderIdAndReceiverId(@Param("senderId") int senderId, @Param("receiverId") int receiverId);

    @Query(value = "SELECT * FROM friendship " +
            "WHERE user_id_receiver = :receiverId AND status = :status", nativeQuery = true)
    List<UserFriend> findPendingRequestsByReceiverAndStatus(@Param("receiverId") int receiverId,
                                                            @Param("status") String status);

    @Query(value = "SELECT * FROM friendship f WHERE user_id_receiver = :receiverId AND user_id_sender = :senderId  AND status = :status", nativeQuery = true)
    UserFriend findByReceiverIdAndRequestIdAndStatus(int receiverId, int senderId,String status);

    @Query(value = "SELECT * FROM users " +
            "WHERE id IN (" +
            "    SELECT DISTINCT friend_id " +
            "    FROM (" +
            "        SELECT CASE " +
            "            WHEN user_id_sender = :userId THEN user_id_receiver " +
            "            ELSE user_id_sender " +
            "        END AS friend_id " +
            "        FROM friendship " +
            "        WHERE status = 'accepted' " +
            "            AND (user_id_receiver = :userId OR user_id_sender = :userId) " +
            "        UNION " +
            "        SELECT :userId AS friend_id" +
            "    ) AS friend_list " +
            "    WHERE friend_id <> :userId" +
            ")", nativeQuery = true)
    List<Object[]> findFriendsById(@Param("userId") int userId);

}
