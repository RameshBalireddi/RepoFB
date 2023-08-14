package post.DTO;

public class FriendResponse {

    private int id;

    private String email;


    private String name;

    public int getId() {
        return id;
    }

    public FriendResponse() {
    }

    public FriendResponse(int id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
