package likelasttime.Bulletin.Board.Config;

import likelasttime.Bulletin.Board.Repository.SpringDataJpaPostRepository;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
    private final SpringDataJpaPostRepository postRepository;

    public SpringConfig(SpringDataJpaPostRepository postRepository){
        this.postRepository=postRepository;
    }

}