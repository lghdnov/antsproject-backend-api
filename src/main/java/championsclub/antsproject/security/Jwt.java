package championsclub.antsproject.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
public class Jwt {

    Dotenv dotenv = Dotenv.load();

    public String createToken(long id, String username, List<String> roles){
        return JWT.create()
                .withSubject(String.valueOf(id))
                .withExpiresAt(Instant.now().plus(Duration.ofDays(3)))
                .withClaim("u", username)
                .withClaim("r", roles)
                .sign(Algorithm.HMAC256(dotenv.get("ANTS_SECURITY_JWT_KEY")));
    }
}
