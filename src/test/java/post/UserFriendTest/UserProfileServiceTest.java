package post.UserFriendTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import post.APIResponse.APIResponse;
import post.DTO.UserDTO;
import post.Entities.UserProfile;
import post.Repositories.PostRepository;
import post.Repositories.UserProfileRepository;
import post.Service.UserProfileService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {

    @InjectMocks
    private UserProfileService userProfileService;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testAddUser_Success() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("john@example.com");
        userDTO.setPassword("password123");

        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userProfileRepository.save(any())).thenReturn(new UserProfile());

        ResponseEntity<APIResponse> response = userProfileService.addUser(userDTO);

        verify(userProfileRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(any());

        // Add assertions to check the response
        // For example:
         assertEquals(HttpStatus.CREATED, response.getStatusCode());
         assertEquals("User added successfully", response.getBody().getMessage());
         assertNotNull(response.getBody().getData());
    }

    @Test
    public void testAddUser_Failure() {
        UserDTO userDTO = new UserDTO();
        userDTO.setName("John Maxie");
        userDTO.setEmail("john@example.com");
        userDTO.setPassword("password123");

        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userProfileRepository.save(any())).thenReturn(new UserProfile());

        ResponseEntity<APIResponse> response = userProfileService.addUser(userDTO);

        verify(userProfileRepository, times(1)).save(any());
        verify(passwordEncoder, times(1)).encode(any());

        // Add assertions to check the response
         assertEquals(HttpStatus.CREATED, response.getStatusCode());
         assertEquals("Failed to add user Please enter valid details.", response.getBody().getMessage());
//       assertNull(response.getBody().getData());
    }





    @Test
    public void testDeleteUserById_Success() {
        int userId = 1;

        UserProfile user = new UserProfile();
        user.setId(userId);
        user.setName("John Doe");
        user.setActive(true);

        when(userProfileRepository.findByIdAndActive(userId, true)).thenReturn(user);
        when(userProfileRepository.save(any())).thenReturn(user);

        ResponseEntity<APIResponse> response = userProfileService.deleteUserById(userId);

        verify(userProfileRepository, times(1)).findByIdAndActive(userId, true);
        verify(userProfileRepository, times(1)).save(user);

        // Add assertions to check the response
        // For example:
         assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
         assertEquals("User deleted successfully", response.getBody().getData());
         assertNotNull(response.getBody().getData());
    }







}
