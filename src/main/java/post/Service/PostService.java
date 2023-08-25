package post.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import post.APIResponse.APIResponse;
import post.DTO.PostRequest;
import post.Entities.Post;
import post.Entities.UserProfile;
import post.Repositories.FollowersRepository;
import post.Repositories.PostRepository;
import post.Repositories.UserProfileRepository;
import post.Responses.PostResponse;
import post.Security.ObjectUtil;

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
    FollowersRepository followersRepository;


    public ResponseEntity<APIResponse> addPost(PostRequest postText, int userId) {
        Optional<UserProfile> userProfile = userProfileRepository.findById(userId);
         Post addpost = new Post();
        addpost.setUser(userProfile.get());
        addpost.setPostText(postText.getPostText());
        addpost.setCreateAt(LocalDateTime.now());
        addpost.setFlag(true);
        postRepository.save(addpost);
        return APIResponse.successCreate("post added successfully", postText);

    }

    public ResponseEntity<APIResponse>  updatePost(int postId, String text) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isEmpty()) {
            return APIResponse.errorNotFound("Posts are not found");
        }
      Post post1=  post.get();
      int postUserId=  post1.getUser().getId();
      int userId= ObjectUtil.getUserId();
      if(postUserId!=userId) {
          return APIResponse.errorUnauthorised("user not allow to update post");
      }
         post1.setPostText(text);
        postRepository.save(post1);
        return APIResponse.success("text updated successfully ", text);
    }

    public ResponseEntity<APIResponse>  getOwnPosts() {


            List<Post> userPosts = postRepository.findByUserId(ObjectUtil.getUserId());

            if (userPosts.isEmpty()) {
                return APIResponse.errorNotFound("Posts are not found");
            }

            List<PostResponse> response = userPosts.stream()
                    .map(dto -> new PostResponse(dto.getId(), dto.getPostText(), dto.getUser().getId()))
                    .collect(Collectors.toList());

            return APIResponse.success("User posts retrieved successfully", response);
        }



    public ResponseEntity<APIResponse>  getFollowingPosts() {
        List<Integer> followingIds = followersRepository.findFollowingIds(ObjectUtil.getUserId());

        if ( followingIds.isEmpty()) {
            return APIResponse.errorNotFound("you couldn't follow any one so posts are not found");
        }
        List<Post> postResult =  postRepository.findAllById(followingIds) ;

        if (postResult.isEmpty()) {
            return APIResponse.errorNotFound("Posts are not found");
        }

        List<PostResponse> responses = postResult.stream()
                .map(dto -> new PostResponse(dto.getId(), dto.getPostText(), dto.getUser().getId()))
                .collect(Collectors.toList());

        return APIResponse.success("Posts retrieved successfully", responses);
    }

    public ResponseEntity<APIResponse>  getAllUserPosts() {

        List<Post> allPosts = postRepository.findAll();

        if (allPosts.isEmpty()) {
            return APIResponse.errorNotFound("Posts are not found");
        }

        List<PostResponse> responses = allPosts.stream()
                .map(dto -> new PostResponse(dto.getId(), dto.getPostText(), dto.getUser().getId()))
                .collect(Collectors.toList());

        return APIResponse.success("Posts retrieved successfully", responses);
    }


    public ResponseEntity<APIResponse>  deletePostById(int postId, int userId) {

         Post post = postRepository.findById(postId).orElse(null);
         if (post==null) {
            return APIResponse.errorNotFound("Post are not found");
        }
       if(post.getUser().getId()==userId){
           return APIResponse.errorUnauthorised("you are not authorise to delete this post");
       }
         post.setFlag(false);
         postRepository.save(post);
         return APIResponse.success("post deleted successfully ",post.getId());
    }

}
















