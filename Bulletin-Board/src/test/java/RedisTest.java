import likelasttime.Bulletin.Board.BulletinBoardApplication;
import likelasttime.Bulletin.Board.Repository.PostRepository;
import likelasttime.Bulletin.Board.Service.PostService;
import likelasttime.Bulletin.Board.domain.posts.Post;
import likelasttime.Bulletin.Board.domain.posts.PostResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(classes= BulletinBoardApplication.class)
public class RedisTest {
    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @AfterEach
    public void afterEach(){
        postService.deleteAll();
    }

    @Test
    public void redisFindByRank(){  // 조회수를 기준으로 상위 10개의 게시글을 출력(캐시 사용)
        // given
        Post post=Post.builder()
                .title("첫 번째")
                .content("안녕")
                .author("im")
                .view(0)
                .comment_cnt(0)
                .build();

        // when
        postRepository.save(post);
        List<PostResponseDto> postList=postService.findByRank();

        // then
        assertThat(postList.get(0).getTitle()).isEqualTo(post.getTitle());
        assertThat(postList.get(0).getContent()).isEqualTo(post.getContent());
        assertThat(postList.get(0).getAuthor()).isEqualTo(post.getAuthor());
        assertThat(postList.get(0).getView()).isEqualTo(post.getView());
        assertThat(postList.get(0).getComment_cnt()).isEqualTo(post.getComment_cnt());
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

        // when
        postRepository.save(post);
        postService.findAll();      // 전체 조회하면 캐시에 저장됨
        postService.deletePost(post.getId());

        // then
        assertThat(postService.findAll().size()).isEqualTo(0);
    }
}
