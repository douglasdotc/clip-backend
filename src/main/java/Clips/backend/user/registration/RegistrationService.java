package Clips.backend.user.registration;

import Clips.backend.user.User;
import Clips.backend.user.UserRole;
import Clips.backend.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RegistrationService {

    // Services injection.
    private final UserService userService;

    // Register a user.
    public User register(RegistrationRequest request) {
        // Email and password already validated in frontend.
        return userService.signUpUser(
            new User(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getAge(),
                request.getPhoneNumber(),
                UserRole.USER
            )
        );
    }
}
