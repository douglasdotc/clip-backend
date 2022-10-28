package Clips.backend.login;

import Clips.backend.user.User;
import Clips.backend.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginService {
    private final UserService userService;

    public User login(LoginRequest request) {
        return userService.loginUser(
            new User(
                request.getEmail(),
                request.getPassword()
            )
        );
    }
}
