package championsclub.antsproject.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class JwtDecoder {
    private final Dotenv dotenv = Dotenv.load();
    public DecodedJWT decode(String token){
        return JWT.require(Algorithm.HMAC256(dotenv.get("ANTS_SECURITY_JWT_KEY")))
                .build().verify(token);
    }
}
