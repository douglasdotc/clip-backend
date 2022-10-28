package Clips.backend.user.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

// A login request from the user.
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LoginRequest {
    @JsonProperty("email")
    private final String email;
    @JsonProperty("password")
    private final String password;
}
