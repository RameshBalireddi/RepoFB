package post.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import post.Repositories.UserProfileRepository;

import java.io.IOException;

@Component
public class CustomUserIdFilter extends OncePerRequestFilter {

    @Autowired
    UserProfileRepository userRepository;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof ApplicationUser) {
                int userId = ((ApplicationUser) authentication.getPrincipal()).getUserId();
                UserIdContextHolder.setUserId(userId);
            }
        } catch (Exception ex) {
            throw new ServletException("An error occurred during authentication: " + ex.getMessage(), ex);
        }
        filterChain.doFilter(request, response);
    }




}



