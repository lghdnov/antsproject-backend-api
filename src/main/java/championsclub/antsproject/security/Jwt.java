package championsclub.antsproject.security;

import championsclub.antsproject.environment.EnvironmentService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
@AllArgsConstructor
public class Jwt {

    private final EnvironmentService env;

    public String createToken(long id, String username, List<String> roles){
        return JWT.create()
                .withSubject(String.valueOf(id))
                .withExpiresAt(Instant.now().plus(Duration.ofDays(3)))
                .withClaim("u", username)
                .withClaim("r", roles)
                .sign(Algorithm.HMAC256(env.get("ANTS_SECURITY_JWT_KEY")));
    }
}
