package post.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import post.Entities.Notification;

import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification,Integer> {
    @Query(value = "select * from notifications where user_id = :userId",nativeQuery = true)
    List<Notification> findByUserId(@Param("userId") int userId);
}
