package Clips.backend.user;

import Clips.backend.responses.MessageResponse;
import Clips.backend.responses.Response;
import Clips.backend.responses.ResponseService;
import Clips.backend.responses.UserInfoResponse;
import Clips.backend.security.jwt.JwtUtils;
import Clips.backend.security.services.UserDetailsImpl;
import Clips.backend.security.services.UserDetailsServiceImpl;
import Clips.backend.user.login.LoginRequest;
import Clips.backend.user.registration.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    @Autowired
    AuthService authService;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    ResponseService responseService;
    @Autowired
    JwtUtils jwtUtils;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
        boolean isRegistered = authService.register(registrationRequest);
        if (isRegistered) {
            return ResponseEntity.ok(
                new MessageResponse("[AuthController|registerUser] User registered successfully!")
            );
        } else {
            return ResponseEntity.badRequest().body(
                new MessageResponse("[AuthController|registerUser] Error: Email is already in use!")
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        // Get UserDetails:
        UserDetailsImpl userDetails = authService.getUserDetails(loginRequest);

        // Generate JWT using user details:
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        // Get the roles of the user:
        List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());

        // Response contains JWT and UserDetails data:
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
            .body(
                new UserInfoResponse(
                    userDetails.getUid(),
                    userDetails.getName(),
                    userDetails.getEmail(),
                    roles
                )
            );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        // Clear the Cookie:
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(
                new MessageResponse("[AuthController|logout] You've been signed out!")
            );
    }

    @GetMapping("/fetchUserByEmail")
    public ResponseEntity<Response> fetchUserByEmail(@RequestParam("email") String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return ResponseEntity.ok(
            responseService.ResponseBuilder(
                of("isUserExist", userDetails != null),
                "[AuthController|fetchUserByEmail] Fetch request sent.",
                OK
            )
        );
    }
}
