package post.Controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import post.Entities.User;
import post.Repositories.UserRepository;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@Valid @RequestBody User user) {

        userRepository.save(user);

        return ResponseEntity.ok("User added successfully");
    }

   @GetMapping("/list")
    public ResponseEntity<?> getUsers() {
       List<User> users = userRepository.findAll();
       if (users.isEmpty()) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("users are not found");
       } else {
           return ResponseEntity.ok(users);
       }

}
}
