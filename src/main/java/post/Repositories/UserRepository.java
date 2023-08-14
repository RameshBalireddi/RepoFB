package post.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import post.Entities.User;

public interface UserRepository extends JpaRepository<User,Integer> {
}
