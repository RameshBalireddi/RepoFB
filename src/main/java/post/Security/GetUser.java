package post.Security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class GetUser {
    public static int getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof ApplicationUser) {
            int userId = ((ApplicationUser) authentication.getPrincipal()).getUserId();
            return userId;
        }
        return 0;
    }
}
