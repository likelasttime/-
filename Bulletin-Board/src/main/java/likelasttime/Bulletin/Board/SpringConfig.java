package likelasttime.Bulletin.Board;

import likelasttime.Bulletin.Board.Repository.PostRepository;
import likelasttime.Bulletin.Board.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
    private final PostRepository postRepository;

    public SpringConfig(PostRepository postRepository){
        this.postRepository=postRepository;
    }

    @Bean
    public PostService postService(){
        return new PostService(postRepository);
    }

}
