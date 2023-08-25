package post.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PasswordDTO {

    @NotNull(message = "oldPassword must not be null")
    private String oldPassword;
    @NotNull(message = "newPassword must not be null")
    private String newPassword;
    @NotNull(message = "confirmPassword must not be null")
    private String confirmPassword;
}
