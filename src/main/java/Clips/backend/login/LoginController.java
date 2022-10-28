package Clips.backend.login;

import Clips.backend.response.Response;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.CREATED;

// RESTful API layer to login a user:
// e.g.: http://localhost:8080/api/v1/login
@RestController
@RequestMapping(path = "api/v1/login")
@AllArgsConstructor
public class LoginController {
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<Response> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(
            Response.builder()
                .timeStamp(now())
                .data(of("response", loginService.login(request)))
                .message("[LoginController|login] Login request sent.")
                .httpStatus(CREATED)
                .statusCode(CREATED.value())
                .build()
        );
    }
}
