package Clips.backend.user;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

// Service for spring security.
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private final static String USER_NOT_FOUND_MSG = "[UserService|%s] User with email %s not found.";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(
                String.format(USER_NOT_FOUND_MSG, "loadUserByUsername", email)
            )
        );
    }

    public User loginUser(User user) {
        // Check if email exist:
        Optional<User> userDetails = userRepository.findByEmail(user.getEmail());
        if (userDetails.isEmpty()) {
            throw new IllegalStateException(
                String.format(USER_NOT_FOUND_MSG, "loadUserByUsername", user.getEmail())
            );
        }

        // Convert Optional user to user:
        User existingUser = userDetails.get();

        // email exist, check password:
        boolean passwordMatch = bCryptPasswordEncoder.matches(user.getPassword(), existingUser.getPassword());
        if (!passwordMatch) {
            throw new IllegalStateException("[UserService|loginUser] Invalid credentials.");
        }
        return existingUser;
    }

    public User signUpUser(User user) {
        // Check if user exists:
        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();

        // Exit and throw exception if yes:
        if (userExists) {
            throw new IllegalStateException("[UserService|signUpUser] Email already taken.");
        }

        // If user not exist, we encode and set the password:
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // Give the user an unique ID:
        String uid = UUID.randomUUID().toString();
        user.setUid(uid);

        // Save the user given information with the above to the database:
        return userRepository.save(user);
    }
}
