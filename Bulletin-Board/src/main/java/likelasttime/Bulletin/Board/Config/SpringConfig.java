package likelasttime.Bulletin.Board.Config;

import likelasttime.Bulletin.Board.Repository.PostRepository;
import likelasttime.Bulletin.Board.Repository.UserRepository;
import likelasttime.Bulletin.Board.Service.PostServiceImpl;
import likelasttime.Bulletin.Board.Service.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SpringConfig {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SpringConfig(UserRepository userRepository, PostRepository postRepository, PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.postRepository=postRepository;
        this.passwordEncoder=passwordEncoder;
    }

    @Bean
    public PostServiceImpl postService(){
        return new PostServiceImpl(postRepository, modelMapper());
    }

    @Bean
    public UserServiceImpl userService(){
        return new UserServiceImpl(passwordEncoder, userRepository);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return modelMapper;
    }
}