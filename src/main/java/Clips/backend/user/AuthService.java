package Clips.backend.user;

import Clips.backend.security.services.UserDetailsImpl;
import Clips.backend.user.login.LoginRequest;
import Clips.backend.user.registration.RegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    PasswordEncoder encoder;

    public boolean register(RegistrationRequest registrationRequest) {
        // Check if email already exist:
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            return false;
        }

        // Create new user:
        User user = new User(
            registrationRequest.getName(),
            registrationRequest.getEmail(),
            encoder.encode(registrationRequest.getPassword()),
            registrationRequest.getAge(),
            registrationRequest.getPhoneNumber()
        );

        // Generate UID and assign roles:
        String uid = UUID.randomUUID().toString();
        user.setUid(uid);

        // We assume people registering are users:
        Set<UserRole> roles = new HashSet<>();
        roles.add(userRoleRepository.findByName(EUserRole.USER).get());
        user.setRoles(roles);

        // Save the new user:
        userRepository.save(user);
        return true;
    }

    public UserDetailsImpl getUserDetails(LoginRequest loginRequest) {
        // Authenticate username (email) and password:
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );

        // Update SecurityContext using Authentication object:
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Get UserDetails from Authentication object:
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return userDetails;
    }
}
