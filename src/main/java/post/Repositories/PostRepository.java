package post.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import post.Entities.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {



    List<Post> findByUserId(Integer id);


    List<Post> findByFlag(boolean b);
}
