package likelasttime.Bulletin.Board.Config;

import likelasttime.Bulletin.Board.domain.posts.Post;
import likelasttime.Bulletin.Board.domain.posts.PostResponseDto;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SpringConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        modelMapper.createTypeMap(Post.class, PostResponseDto.class)
                .addMapping(Post :: getId, PostResponseDto :: setPostId)
                .addMapping(Post :: getTitle, PostResponseDto :: setPostTitle)
                .addMapping(Post :: getContent, PostResponseDto :: setPostContent);

        return modelMapper;
    }
}