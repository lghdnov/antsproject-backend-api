package championsclub.antsproject.controller;

import championsclub.antsproject.data.domain.User;
import championsclub.antsproject.data.repository.UserRepo;
import championsclub.antsproject.model.LoginRequest;
import championsclub.antsproject.model.LoginResponse;
import championsclub.antsproject.model.RegisterRequest;
import championsclub.antsproject.model.RegisterResponse;
import championsclub.antsproject.security.Jwt;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class Auth {
    private final Dotenv env = Dotenv.load();

    private final Jwt jwt;

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest request){

        String salit = env.get("ANTS_SECURITY_HASH_SALT");
        String hash = BCrypt.hashpw(request.getPassword(), salit);
        String hash1 = BCrypt.hashpw(request.getPassword(), salit);
        User user = userRepo.findByUsername(request.getUsername());
        if (user == null || !user.getPassword().equals(hash))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        return LoginResponse.builder()
                .token(jwt.createToken(user.getId(), request.getUsername(), List.of("USER")))
                .build();
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody @Validated RegisterRequest request){

        if (request == null || request.getPassword() == null || request.getUsername() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        if (userRepo.existsByUsername(request.getUsername()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);

        String salit = env.get("ANTS_SECURITY_HASH_SALT");
        String hashedPassword = BCrypt.hashpw(request.getPassword(), salit);

        User user = new User(
                request.getUsername(),
                hashedPassword
        );

        userRepo.save(user);

        return RegisterResponse.builder()
                .token(jwt.createToken(user.getId(), user.getUsername(), List.of("USER")))
                .build();
    }
}