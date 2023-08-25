package post.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import post.Entities.UserProfile;
import post.Responses.UserWithPostCountResponse;

import java.util.List;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Integer> {
      UserProfile findByEmail(String email);

    @Query("SELECT new post.Responses.UserWithPostCountResponse(u.id, u.name, u.email, u.profilePicPath, COUNT(p)) " +
            "FROM UserProfile u " +
            "LEFT JOIN Post p ON u.id = p.user.id " +
            "GROUP BY u.id")
    List<UserWithPostCountResponse> getUsersWithPostCount();
    }