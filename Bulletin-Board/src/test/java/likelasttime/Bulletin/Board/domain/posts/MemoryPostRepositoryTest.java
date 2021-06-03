package likelasttime.Bulletin.Board.domain.posts;

import likelasttime.Bulletin.Board.Repository.MemoryPostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class MemoryPostRepositoryTest {
    MemoryPostRepository repository=new MemoryPostRepository();

    @AfterEach
    public void afterEach(){
        repository.clearPost();
    }

    @Test
    public void save(){
        Post post =new Post();
        post.setTitle("spring");

        repository.save(post);
        Post result=repository.findById(post.getId()).get();
        Assertions.assertThat(result).isEqualTo(post);

    }

    @Test
    public void findByName(){
        Post post1 =new Post();
        post1.setTitle("spring1");
        repository.save(post1);

        Post post2 =new Post();
        post2.setTitle("spring2");
        repository.save(post2);

        Post result=repository.findByTitle("spring1").get();
        Assertions.assertThat(result).isEqualTo(post1);
    }

    @Test
    public void findAll(){
        Post post1 =new Post();
        post1.setTitle("love");
        repository.save(post1);

        Post post2 =new Post();
        post2.setTitle("love you");
        repository.save(post2);

        List<Post> result=repository.findAll();
        Assertions.assertThat(result.size()).isEqualTo(2);

    }

}

