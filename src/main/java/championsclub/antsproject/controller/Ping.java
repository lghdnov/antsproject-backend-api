package championsclub.antsproject.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Ping {
    @PostMapping("/ping")
    public String ping(){
        return "Pong!";
    }
    @PostMapping("/authping")
    public String authPing(){
        return "Auth Pong!";
    }
}
