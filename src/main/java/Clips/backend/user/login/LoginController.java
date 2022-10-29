package Clips.backend.user.login;

import Clips.backend.response.Response;
import Clips.backend.response.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;

// RESTful API layer to login a user:
// e.g.: http://localhost:8080/api/v1/login
@RestController
@RequestMapping(path = "api/v1/user/login")
@AllArgsConstructor
public class LoginController {
    private LoginService loginService;
    private ResponseService responseService;

    @PostMapping
    public ResponseEntity<Response> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(responseService.ResponseBuilder(
            of("response", loginService.login(request)),
            "[LoginController|login] Login request sent.",
            CREATED
        ));
    }
}
