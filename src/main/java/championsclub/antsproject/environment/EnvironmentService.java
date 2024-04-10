package championsclub.antsproject.environment;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnvironmentService {
    private Optional<Dotenv> dotenv = Optional.empty();

    public EnvironmentService(){
        try {
            dotenv = Optional.of(Dotenv.configure().load());
        }
        catch (DotenvException ignored) {}
    }

    public String get(String val){
        if (dotenv.isPresent())
            return dotenv.get().get(val);
        else
            return System.getenv(val);
    }
}
