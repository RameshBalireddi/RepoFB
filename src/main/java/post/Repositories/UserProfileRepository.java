package post.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import post.Entities.UserProfile;

import java.util.List;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile,Integer> {
    UserProfile findByEmail(String email);

    List<UserProfile> findByActive(boolean b);
}