package post.Responses;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PendingRequest {

    @NotNull
    private int senderId;

    private String status;

    private String senderName;

    private int receiverId;


}
