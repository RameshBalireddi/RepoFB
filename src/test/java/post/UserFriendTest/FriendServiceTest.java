//package post.UserFriendTest;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.*;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import post.APIResponse.APIResponse;
//import post.Entities.UserProfile;
//import post.Repositories.UserFriendRepository;
//import post.Repositories.UserProfileRepository;
//import post.Responses.FriendResponse;
//import post.Service.UserFriendService;
//
//public class FriendServiceTest{
//
//    @InjectMocks
//    private UserFriendService friendService;
//
//    @Mock
//    private UserFriendRepository userFriendRepository;
//
//    @Mock
//    private UserProfileRepository userProfileRepository;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testGetFriendsByUserId() {
//        int userId = 1;
//
//        List<Integer> friendIds = Arrays.asList(2, 3); // Sample friend IDs
//        when(userFriendRepository.findFriendIds(userId)).thenReturn(friendIds);
//
//        UserProfile userProfile1 = new UserProfile(2, "Friend 1", "friend1@example.com", "bunny","/path/to/friend1.jpg","url",true);
//        UserProfile userProfile2 = new UserProfile(3, "Friend 2", "friend2@example.com","bunny", "/path/to/friend2.jpg","url",true);
//        List<UserProfile> userProfiles = Arrays.asList(userProfile1, userProfile2);
//        when(userProfileRepository.findAllById(friendIds)).thenReturn(userProfiles);
//
//        List<FriendResponse> expectedFriendResponses = Arrays.asList(
//                new FriendResponse(2, "Friend 1", "friend1@example.com", "/path/to/friend1.jpg"),
//                new FriendResponse(3, "Friend 2", "friend2@example.com", "/path/to/friend2.jpg")
//        );
//
//        ResponseEntity<APIResponse> responseEntity = friendService.getFriendsByUserId(userId);
//
//        List<FriendResponse> actualFriendResponses = (List<FriendResponse>) responseEntity.getBody().getData();
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertNotNull(responseEntity.getBody());
//        assertEquals(expectedFriendResponses, actualFriendResponses);
//
//    }
//
//
//
//}
