package post.Security;

public class UserIdContextHolder {

    private static final ThreadLocal<Integer> userIdHolder = new ThreadLocal<>();

    public static void setUserId(int userId) {
        userIdHolder.set(userId);
    }

    public static int getUserId() {
        return userIdHolder.get();
    }

    public static void clearUserId() {
        userIdHolder.remove();
    }
}

