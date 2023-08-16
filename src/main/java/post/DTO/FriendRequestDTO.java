package post.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FriendRequestDTO {

    private int senderId;
    private String status;

    private String senderName;

}
