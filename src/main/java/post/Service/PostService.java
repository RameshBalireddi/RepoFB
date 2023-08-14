package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import post.DTO.PostResponse;
import post.Entities.Post;
import post.Repositories.PostRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Service
public class PostService {


    @Autowired
    PostRepository postRepository;

    public static PostResponse convertToPostResponse(Object[] postAndCount) {
        int postId = (int) postAndCount[0];
        String postContent = (String) postAndCount[1];
        int userId = (int) postAndCount[2];
        int likesCount = ((Long) postAndCount[3]).intValue();
        int commentsCount = ((Long) postAndCount[4]).intValue();

        return new PostResponse(postId, postContent,userId,likesCount,commentsCount);
    }
        public ResponseEntity<?> getAllPosts(int userId) {
            List<Object[]> rawPosts = postRepository.getPostsAndCountsByUser(userId);
            if(rawPosts.isEmpty()){
                return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("posts are not found ");
            }
            List<PostResponse> postResponses = new ArrayList<>();

            for (Object[] postAndCount : rawPosts) {
                PostResponse postResponse =convertToPostResponse(postAndCount);
                postResponses.add(postResponse);
            }

            return ResponseEntity.ok(postResponses);
        }



}
