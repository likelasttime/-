import likelasttime.Bulletin.Board.BulletinBoardApplication;
import likelasttime.Bulletin.Board.Repository.PostRepository;
import likelasttime.Bulletin.Board.Service.PostService;
import likelasttime.Bulletin.Board.domain.posts.Post;
import likelasttime.Bulletin.Board.domain.posts.PostRequestDto;
import likelasttime.Bulletin.Board.domain.posts.PostResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes= BulletinBoardApplication.class)
public class RedisTest {
    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @AfterEach
    public void afterEach(){
        postService.deleteAll();
    }

    @Test
    public void redisFindByRank(){  // 조회수를 기준으로 상위 10개의 게시글을 출력(캐시 사용)
        // given
        Post post1=Post.builder()
                .title("두 번째")
                .content("안녕")
                .author("im")
                .view(1)
                .comment_cnt(0)
                .build();

        Post post2=Post.builder()
                .title("첫 번째")
                .content("안녕")
                .author("im")
                .view(2)
                .comment_cnt(0)
                .build();

        postRepository.save(post1);
        PostResponseDto postResponseDto1=modelMapper.map(post1, PostResponseDto.class);
        redisTemplate.opsForZSet().add("findByRank", post1.getId().toString(), post1.getView());
        redisTemplate.opsForHash().put("rankByHash", post1.getId().toString(), postResponseDto1);

        postRepository.save(post2);
        PostResponseDto postResponseDto2=modelMapper.map(post2, PostResponseDto.class);
        redisTemplate.opsForZSet().add("findByRank", post2.getId().toString(), post2.getView());
        redisTemplate.opsForHash().put("rankByHash", post2.getId().toString(), postResponseDto2);

        // when
        List<PostResponseDto> postList=postService.findByRank();

        // then
        assertThat(postList.get(0).getTitle()).isEqualTo(postResponseDto2.getTitle());
        assertThat(postList.get(1).getTitle()).isEqualTo(postResponseDto1.getTitle());
    }

    @Test
    public void redisDeletePost(){
        // given
        Post post=Post.builder()
                .title("첫 번째")
                .content("안녕")
                .author("im")
                .view(0)
                .comment_cnt(0)
                .build();

        PostResponseDto postResponseDto=modelMapper.map(post, PostResponseDto.class);
        postRepository.save(post);
        Long id=post.getId();

        redisTemplate.opsForZSet().add("findByRank", id.toString(), post.getView());
        redisTemplate.opsForHash().put("rankByHash", id.toString(), postResponseDto);

        // when
        postService.deletePost(post.getId());

        // then
        assertThat(redisTemplate.opsForHash().hasKey("rankByHash", id.toString())).isEqualTo(false);
        assertThat(redisTemplate.opsForZSet().size("findByRank")).isEqualTo(0);

    }

    @Test
    public void redisUpdatePost(){
        // given
        Post post=Post.builder()
                .title("첫 번째")
                .content("안녕")
                .author("im")
                .view(0)
                .comment_cnt(0)
                .build();

        postRepository.save(post);
        PostRequestDto postRequestDto=modelMapper.map(post, PostRequestDto.class);
        PostResponseDto postResponseDto=modelMapper.map(post, PostResponseDto.class);
        String id=postResponseDto.getId().toString();

        redisTemplate.opsForHash().put("rankByHash", id, postResponseDto);
        PostResponseDto hashValue1=(PostResponseDto) redisTemplate.opsForHash().get("rankByHash", id);
        postRequestDto.setTitle("첫 번째(수정)");
        postResponseDto.setTitle("첫 번째(수정)");

        // when
        PostResponseDto afterPostResponseDto=postService.update(Long.valueOf(id), postRequestDto);          // 수정

        // then
        PostResponseDto hashValue2=(PostResponseDto) redisTemplate.opsForHash().get("rankByHash", id);
        assertThat(hashValue1.getTitle()).isNotEqualTo(hashValue2.getTitle());
        assertThat(afterPostResponseDto.getTitle()).isEqualTo("첫 번째(수정)");

    }

    @Test
    public void redisWritePost(){
        // given
        Post post=Post.builder()
                .title("첫 번째")
                .content("안녕")
                .author("im")
                .view(0)
                .comment_cnt(0)
                .build();

        postRepository.save(post);
        PostRequestDto postRequestDto=modelMapper.map(post, PostRequestDto.class);

        // when
        PostResponseDto postResponseDto=postService.create(postRequestDto);

        // then
        PostResponseDto cacheDto=(PostResponseDto) redisTemplate.opsForHash().get("findAll", post.getId().toString());
        assertThat(cacheDto.getTitle()).isEqualTo(post.getTitle());
        assertThat(cacheDto.getContent()).isEqualTo(post.getContent());
        assertThat(cacheDto.getAuthor()).isEqualTo(post.getAuthor());
    }

    @Test
    public void redisFindAll(){     // DB 조회 후 캐시에 저장
        // given
        Post post1=Post.builder()
                .title("두 번째")
                .content("안녕")
                .author("im")
                .view(1)
                .comment_cnt(0)
                .build();

        Post post2=Post.builder()
                .title("첫 번째")
                .content("안녕")
                .author("im")
                .view(2)
                .comment_cnt(0)
                .build();

        // when
        postRepository.save(post1);
        postRepository.save(post2);
        List<PostResponseDto> postResponseDtoList=postService.findAll();

        // then
        assertThat(redisTemplate.opsForHash().size("findAll")).isEqualTo(2);
    }

    @Test
    public void RedisFindById() throws IOException {
        // given
        Post post1=Post.builder()
                .title("두 번째")
                .content("안녕")
                .author("im")
                .view(1)
                .comment_cnt(0)
                .build();

        // when
        postRepository.save(post1);
        Long id=post1.getId();
        postService.findById(id);

        // then
        assertThat(redisTemplate.opsForHash().size("findAll")).isEqualTo(1);
        assertThat(redisTemplate.opsForHash().size("rankByHash")).isEqualTo(1);
        assertThat(redisTemplate.opsForZSet().size("findByRank")).isEqualTo(1);
    }
}
