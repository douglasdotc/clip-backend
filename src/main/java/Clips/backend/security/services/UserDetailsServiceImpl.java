package Clips.backend.security.services;

import Clips.backend.user.User;
import Clips.backend.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find if given user exist:
        Optional<User> user = userRepository.findByEmail(username);

        if (user.isEmpty()) {
            return null;
        }
        // If yes, build the user:
        return UserDetailsImpl.build(user.get());
    }
}
