package championsclub.antsproject.security;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtConverter {

    public UserPrincipal convert(DecodedJWT jwt){
        return UserPrincipal.builder()
                .id(Long.parseLong(jwt.getSubject()))
                .username(jwt.getClaim("u").asString())
                .authorities(getAuthorities(jwt))
                .build();
    }

    private List<SimpleGrantedAuthority> getAuthorities(DecodedJWT jwt){
        Claim claim = jwt.getClaim("r");
        return (claim.isNull() || claim.isMissing()) ?
                List.of() : claim.asList(SimpleGrantedAuthority.class);
    }
}
