package post.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;


@Data
public class UserDTO {

    private String name;

    @Email(message = "Invalid email format")
    private String email;

    private String password;
}
