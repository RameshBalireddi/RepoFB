package post.Enum;

public enum FriendshipStatus {
    PENDING("pending"),
    ACCEPTED("accepted"),
    REJECTED("rejected");

    private final String status;

    FriendshipStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}

