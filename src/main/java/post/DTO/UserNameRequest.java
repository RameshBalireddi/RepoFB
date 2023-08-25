package post.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserNameRequest {

    @NotNull
    private String userName;
}
