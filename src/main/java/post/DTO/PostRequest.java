package post.DTO;

import jakarta.validation.constraints.NotNull;

public class PostRequest {
    @NotNull(message = "postText must not be null")
    private String postText;

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }
}
