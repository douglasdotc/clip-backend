package Clips.backend.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

// A register request from the user. Different from User model.
// This is what the user can give us. We will fuse in more information and save all of them in the database.
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequest {
    @JsonProperty("name")
    private final String name;
    @JsonProperty("email")
    private final String email;
    @JsonProperty("password")
    private final String password;
    @JsonProperty("age")
    private final Integer age;
    @JsonProperty("phone_number")
    private final String phoneNumber;
}
