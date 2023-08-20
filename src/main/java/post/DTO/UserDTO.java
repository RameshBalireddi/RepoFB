package post.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDTO {

    @NotNull
    private String name;

    @Email(message = "Invalid email format")
    private String email;
    @NotNull
    private String password;
}
