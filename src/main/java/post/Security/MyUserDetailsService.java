package post.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import post.Entities.UserProfile;
import post.Repositories.UserProfileRepository;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserProfileRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserProfile> user = Optional.ofNullable(userRepository.findByEmail(email));
        if (user == null) {
            throw new UsernameNotFoundException("user notFound");
        }
        return user.map(ApplicationUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("user not found" + email));
    }

}

