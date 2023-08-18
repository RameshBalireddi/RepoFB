package post.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;
import post.DTO.FriendsDTO;
import post.Entities.UserFriend;
import post.Entities.UserProfile;
import post.Responses.FriendResponse;

import java.util.List;

@Repository
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

    @Query(value = "SELECT u.id, u.name, u.email, u.profile_pic_path " +
            "FROM users u " +
            "WHERE u.id IN (" +
            "    SELECT DISTINCT friend_id " +
            "    FROM (" +
            "        SELECT CASE " +
            "            WHEN f.user_id_sender = :userId THEN f.user_id_receiver " +
            "            ELSE f.user_id_sender " +
            "        END AS friend_id " +
            "        FROM friendship f " +
            "        WHERE f.status = 'accepted' " +
            "            AND (f.user_id_receiver = :userId OR f.user_id_sender = :userId) " +
            "        UNION " +
            "        SELECT :userId AS friend_id" +
            "    ) AS friend_list " +
            "    WHERE friend_id <> :userId" +
            ")", nativeQuery = true)
     List<FriendResponse> findFriendsById(@Param("userId") int userId);


    @Query(value = "SELECT DISTINCT friend_id " +
            "FROM (" +
            "    SELECT CASE " +
            "        WHEN user_id_sender = :userId THEN user_id_receiver " +
            "        ELSE user_id_sender " +
            "    END AS friend_id " +
            "    FROM friendship " +
            "    WHERE status = 'accepted' " +
            "        AND (user_id_receiver = :userId OR user_id_sender = :userId) " +
            "    UNION " +
            "    SELECT :userId AS friend_id" +
            ") AS friend_list " +
            "WHERE friend_id <> :userId", nativeQuery = true)
    List<Integer> findFriendIds(@Param("userId") int userId);


}
