package likelasttime.Bulletin.Board.Config;

import likelasttime.Bulletin.Board.domain.posts.Post;
import likelasttime.Bulletin.Board.domain.posts.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CustomModelMapper {
    private final ModelMapper modelMapper;

    @Bean
    public ModelMapper standardMapper(){
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STANDARD);
        modelMapper.createTypeMap(Post.class, PostResponseDto.class)
                .addMapping(Post :: getId, PostResponseDto :: setPostId)
                .addMapping(Post :: getTitle, PostResponseDto :: setPostTitle)
                .addMapping(Post :: getContent, PostResponseDto :: setPostContent);

        return modelMapper;
    }

}
