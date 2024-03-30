package championsclub.antsproject.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RegisterRequest {
    private String username;
    private String password;
}
