package post.DTO;

public class PostResponse {

  private int  postId;
    private String post;
  private int userId;


  private int likes;

  private int comments;

      public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }


    public PostResponse(int postId, String post, int userId, int likes, int comments) {
        this.postId = postId;
        this.post = post;
        this.userId = userId;
        this.likes = likes;
        this.comments = comments;
    }

    public PostResponse() {
    }
}
