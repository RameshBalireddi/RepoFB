package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import post.APIResponse.APIResponse;
import post.Entities.Post;
import post.Entities.UserFriend;
import post.Entities.UserProfile;
import post.Enum.FriendshipStatus;
import post.Repositories.PostRepository;
import post.Repositories.UserFriendRepository;
import post.Repositories.UserProfileRepository;
import post.Responses.PostResponse;
import post.Security.GetUser;

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
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("post added successfully", postText)).getBody();

    }

    public ResponseEntity<APIResponse>  updatePost(int postId, String text) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("Posts are not found")).getBody();
        }
      Post post1=  post.get();
      int postUserId=  post1.getUser().getId();
      int userId= GetUser.getUserId();
      if(postUserId!=userId) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error("user not allow to update post")).getBody();
      }
         post1.setPostText(text);
        postRepository.save(post1);
        return APIResponse.success("text updated successfully ", text);
    }


    public ResponseEntity<APIResponse>  getAllPosts(int userId, boolean friends,boolean own) {

        if (own) {
            List<Post> userPosts = postRepository.findByUserId(userId);

            if (userPosts.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("Posts are not found")).getBody();
            }

            List<PostResponse> response = userPosts.stream()
                    .map(dto -> new PostResponse(dto.getId(), dto.getPostText(), dto.getUser().getId()))
                    .collect(Collectors.toList());

            return APIResponse.success("User posts retrieved successfully", response);
        }
        List<Integer> friendsIds = userFriendRepository.findFriendIds(userId);

        if (friends && friendsIds.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("this user have no friends")).getBody();
        }

        List<Post> postResult = friends ? postRepository.findAllById(friendsIds) : postRepository.findByFlag(true);

        if (postResult.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("Posts are not found")).getBody();
        }

        List<PostResponse> response = postResult.stream()
                .map(dto -> new PostResponse(dto.getId(), dto.getPostText(), dto.getUser().getId()))
                .collect(Collectors.toList());

        return APIResponse.success("Posts retrieved successfully", response);
    }


    public ResponseEntity<APIResponse>  deletePostById(int postId, int userId) {

         Optional<Post> post = postRepository.findById(postId);
         if ((post.isEmpty()))
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.error("Posts are not found")).getBody();

        int postUserId=post.get().getId();
      UserFriend userFriend=  userFriendRepository.findByReceiverIdAndRequestIdAndStatus(postUserId,userId, String.valueOf(FriendshipStatus.ACCEPTED));
       if(userFriend==null)
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(APIResponse.error("you are not authorise to delete this post")).getBody();
         Post post1= post.get();
         post1.setFlag(false);
         postRepository.save(post1);
         return APIResponse.success("post deleted successfully ",post1.getId());
    }

}
















