package Clips.backend.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class UserInfoResponse {
    private String uid;
    private String name;
    private String email;
    private List<String> roles;
}
