package Clips.backend.security.services;

import Clips.backend.user.User;
import Clips.backend.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find if given user exist:
        User user = userRepository.findByEmail(username)
            .orElseThrow(
                () -> new UsernameNotFoundException(
                    "User Not Found with username (email): " + username
                )
            );

        // If yes, build the user:
        return UserDetailsImpl.build(user);
    }
}
