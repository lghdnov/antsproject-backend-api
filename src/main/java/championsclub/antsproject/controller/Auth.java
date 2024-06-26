package championsclub.antsproject.controller;

import championsclub.antsproject.data.domain.User;
import championsclub.antsproject.data.repository.UserRepo;
import championsclub.antsproject.environment.EnvironmentService;
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
    private final EnvironmentService env;
    private final Jwt jwt;

    @Autowired
    private UserRepo userRepo;

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Validated LoginRequest request){

        User user = userRepo.findByUsername(request.getUsername());
        if (user == null || !user.getPassword().equals(BCrypt.hashpw(request.getPassword(), env.get("ANTS_SECURITY_HASH_SALT"))))
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

        User user = new User(
                request.getUsername(),
                BCrypt.hashpw(request.getPassword(), env.get("ANTS_SECURITY_HASH_SALT"))
        );

        userRepo.save(user);

        return RegisterResponse.builder()
                .token(jwt.createToken(user.getId(), user.getUsername(), List.of("USER")))
                .build();
    }
}