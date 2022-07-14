import likelasttime.Bulletin.Board.BulletinBoardApplication;
import likelasttime.Bulletin.Board.Repository.PostRepository;
import likelasttime.Bulletin.Board.Service.PostService;
import likelasttime.Bulletin.Board.domain.posts.Post;
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
    public void afterAll(){
        postRepository.deleteAll();
    }

    @Test
    public void redisFindByRank(){
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
        List<Post> postList=postService.findByRank();

        // then
        assertThat(postList.get(0).getTitle()).isEqualTo("첫 번째");
        assertThat(postList.get(0).getContent()).isEqualTo("안녕");
        assertThat(postList.get(0).getAuthor()).isEqualTo("im");

    }
}
