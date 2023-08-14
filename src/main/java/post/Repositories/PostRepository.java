//package post.Repositories;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import post.DTO.PostResponse;
//import post.Entities.Post;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public interface PostRepository extends JpaRepository<Post,Integer> {
////    @Query(value = "SELECT DISTINCT p.id AS post_id, p.post AS post, p.user_id AS shared_user, " +
////            "COUNT(DISTINCT l.id) AS like_count, COUNT(DISTINCT c.id) AS comment_count " +
////            "FROM friendship f " +
////            "JOIN posts p ON (f.user_id_receiver = p.user_id OR f.user_id_sender = p.user_id) " +
////            "LEFT JOIN likes l ON p.id = l.post_id " +
////            "LEFT JOIN comments c ON p.id = c.post_id " +
////            "WHERE f.status = 'accepted' " +
////            "AND (f.user_id_receiver = :userId OR f.user_id_sender = :userId) " +
////            "GROUP BY p.id, p.post, p.user_id", nativeQuery = true)
////    List<ArrayList> getPostsAndCountsByUser(@Param("userId") int userId);
//
////    @Query(value = "SELECT p.id, p.user_id, p.post " +
////            "FROM friendship f " +
////            "JOIN posts p ON (f.user_id_receiver = p.user_id OR f.user_id_sender = p.user_id) " +
////            "WHERE f.status = 'accepted' " +
////            "AND (f.user_id_receiver = :userId OR f.user_id_sender = :userId)", nativeQuery = true)
////    List<PostResponse> getPostsForUser(@Param("userId") int userId);
////    @Query(value = "SELECT DISTINCT p.id AS post_id, p.post AS post, p.user_id AS shared_user, " +
////            "COUNT(DISTINCT l.id) AS like_count, COUNT(DISTINCT c.id) AS comment_count " +
////            "FROM friendship f " +
////            "JOIN posts p ON (f.user_id_receiver = p.user_id OR f.user_id_sender = p.user_id) " +
////            "LEFT JOIN likes l ON p.id = l.post_id " +
////            "LEFT JOIN comments c ON p.id = c.post_id " +
////            "WHERE f.status = 'accepted' " +
////            "AND (f.user_id_receiver = :userId OR f.user_id_sender = :userId) " +
////            "GROUP BY p.id, p.post, p.user_id", nativeQuery = true)
////    List<ArrayList> getPostsAndCountsByUser(@Param("userId") int userId);
//
//        @Query(value =
//                "SELECT new post.DTO.PostResponse(p.id, p.user_id, p.post) " +
//                        "FROM friendship f " +
//                        "JOIN posts p ON (f.user_id_receiver = p.user_id OR f.user_id_sender = p.user_id) " +
//                        "WHERE f.status = 'accepted' " +
//                        "AND (f.user_id_receiver = :userId OR f.user_id_sender = :userId)",
//                nativeQuery = true)
//        List<PostResponse> getPostsForUser(@Param("userId") int userId);
//
//}


package post.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import post.DTO.PostResponse;
import post.Entities.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    //get  post likes count and comments count
    @Query(value = "SELECT DISTINCT " +
            "       p.id AS post_id, " +
            "       p.post AS post, " +
            "       p.user_id AS shared_user, " +
            "       COUNT(DISTINCT l.id) AS like_count, " +
            "       COUNT(DISTINCT c.id) AS comment_count " +
            "FROM friendship f " +
            "JOIN posts p ON (f.user_id_receiver = p.user_id OR f.user_id_sender = p.user_id) " +
            "LEFT JOIN likes l ON p.id = l.post_Id " +
            "LEFT JOIN comments c ON p.id = c.post_id " +
            "WHERE f.status = 'accepted' " +
            "    AND (f.user_id_receiver = :userId OR f.user_id_sender = :userId) " +
            "GROUP BY p.id, p.post, p.user_id", nativeQuery = true)
    List<Object[]> getPostsAndCountsByUser(@Param("userId") int userId);

}
