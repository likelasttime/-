package likelasttime.Bulletin.Board.domain.posts;

import likelasttime.Bulletin.Board.Repository.MemoryPostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class MemoryPostRepositoryTest {
    MemoryPostRepository repository=new MemoryPostRepository();

    @AfterEach
    public void afterEach(){
        repository.clearBlog();
    }

    @Test
    public void save(){
        // given
        Post post =new Post();
        post.setTitle("spring");

        // when
        repository.save(post);

        // then
        Post result=repository.findById(post.getId()).get();
        assertThat(result).isEqualTo(post);

    }

    @Test
    public void findById(){
        // given
        Post post1=new Post();
        post1.setTitle("spring1");
        repository.save(post1);

        Post post2=new Post();
        post2.setTitle("spring2");
        repository.save(post2);

        // when
        Post result=repository.findById(post1.getId()).get();

        // then
        assertThat(result).isEqualTo(post1);
    }

    @Test
    public void findByTitle(){
        // given
        Post post1 =new Post();
        post1.setTitle("spring1");
        repository.save(post1);

        Post post2 =new Post();
        post2.setTitle("spring2");
        repository.save(post2);

        // when
        Post result=repository.findByTitle("spring1").get();

        // then
        assertThat(result).isEqualTo(post1);
    }

    @Test
    public void findAll(){
        // given
        Post post1 =new Post();
        post1.setTitle("love");
        repository.save(post1);

        Post post2 =new Post();
        post2.setTitle("love you");
        repository.save(post2);

        // when
        List<Post> result=repository.findAll();

        // then
        assertThat(result.size()).isEqualTo(2);

    }

    @Test
    public void update(){
        //given
        Post post1=new Post();
        post1.setTitle("Spring0");
        repository.save(post1);
        post1.setTitle("Spring1");      // 수정

        // when
        repository.update(post1.getId(), post1);

        // then
        assertThat(post1.getTitle()).isEqualTo("Spring1");
    }

    @Test
    public void delete(){
        //given
        Post post1=new Post();
        post1.setTitle("Spring1");
        repository.save(post1);

        // when
        repository.delete(post1.getId());

        // then
        assertThat(repository.findByTitle("Spring1").empty());

    }

}

