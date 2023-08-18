package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import post.APIResponse.APIResponse;
import post.DTO.PostDTO;
import post.Entities.Post;
import post.Entities.UserFriend;
import post.Entities.UserProfile;
import post.Enum.FriendshipStatus;
import post.Repositories.PostRepository;
import post.Repositories.UserFriendRepository;
import post.Repositories.UserProfileRepository;
import post.Responses.PostResponse;
import post.Security.UserIdContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PostService {


    @Autowired
    PostRepository postRepository;
    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    UserFriendRepository userFriendRepository;


    public ResponseEntity<APIResponse> addPost(String postText,int userId) {
        Optional<UserProfile> userProfile = userProfileRepository.findById(userId);

         Post addpost = new Post();
        addpost.setUser(userProfile.get());
        addpost.setPostText(postText);
        addpost.setCreateAt(LocalDateTime.now());
        addpost.setFlag(true);
        postRepository.save(addpost);
        return APIResponse.success("post added successfully", postText);

    }

    public ResponseEntity<APIResponse> updatePost(int postId, String text) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            return APIResponse.error("post not available ");
        }
      Post post1=  post.get();
      int postUserId=  post1.getUser().getId();
      int userId= UserIdContextHolder.getUserId();
      if(postUserId!=userId) {
          return APIResponse.error("user not allowed to update ");
      }
         post1.setPostText(text);
        postRepository.save(post1);
        return APIResponse.success("text updated successfully ", text);
    }


    public APIResponse getAllPosts(int userId) {
        try {

            List<Integer> friendsIds = userFriendRepository.findFriendIds(userId);
            List<Post> postResult = postRepository.findAllById(friendsIds);
            if(friendsIds.isEmpty())
                return APIResponse.error("this user have no friends").getBody();

            if (postResult.isEmpty()) {
                return APIResponse.error("Posts are not found").getBody();
            }


            List<PostResponse> response = postResult.stream()
                    .map(dto -> new PostResponse(
                            dto.getId(), dto.getPostText(), dto.getUser().getId()

                    ))
                    .collect(Collectors.toList());

            return APIResponse.success("Posts retrieved successfully", response).getBody();
        } catch (Exception e) {
            return APIResponse.error("Failed to fetch posts: " + e.getMessage()).getBody();
        }
    }

    public APIResponse deletePostById(int postId, int userId) {

         Optional<Post> post = postRepository.findById(postId);
         if ((post.isEmpty()))
             return APIResponse.error("post not found").getBody();

        int postUserId=post.get().getId();
      UserFriend userFriend=  userFriendRepository.findByReceiverIdAndRequestIdAndStatus(postUserId,userId, String.valueOf(FriendshipStatus.ACCEPTED));
       if(userFriend==null)
         return APIResponse.error("user cant delete this post").getBody();
         Post post1= post.get();
         post1.setFlag(false);
         postRepository.save(post1);
         return APIResponse.success("post deleted successfully ",post1.getId()).getBody();
    }
}
















