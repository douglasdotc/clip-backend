package Clips.backend.user.registration;

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

// RESTful API layer to register a user.
// e.g.: http://localhost:8080/api/v1/registration
@RestController
@RequestMapping(path = "api/v1/user/registration")
@AllArgsConstructor
public class RegistrationController {
    private RegistrationService registrationService;
    private ResponseService responseService;

    @PostMapping
    public ResponseEntity<Response> register(@RequestBody RegistrationRequest request) {
        // Get a json from frontend and register the user using the registration service
        return ResponseEntity.ok(responseService.ResponseBuilder(
            of("response", registrationService.register(request)),
            "[RegistrationController|register] Registration request sent.",
            CREATED
        ));
    }
}
