package likelasttime.Bulletin.Board.domain.posts;

import likelasttime.Bulletin.Board.Repository.MemoryPostsRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class MemoryPostsRepositoryTest{
    MemoryPostsRepository repository=new MemoryPostsRepository();

    @AfterEach
    public void afterEach(){
        repository.clearPost();
    }

    @Test
    public void save(){
        Posts posts=new Posts();
        posts.setTitle("spring");

        repository.save(posts);
        Posts result=repository.findById(posts.getId()).get();
        Assertions.assertThat(result).isEqualTo(posts);

    }

    @Test
    public void findByName(){
        Posts posts1=new Posts();
        posts1.setTitle("spring1");
        repository.save(posts1);

        Posts posts2=new Posts();
        posts2.setTitle("spring2");
        repository.save(posts2);

        Posts result=repository.findByTitle("spring1").get();
        Assertions.assertThat(result).isEqualTo(posts1);
    }

    @Test
    public void findAll(){
        Posts posts1=new Posts();
        posts1.setTitle("love");
        repository.save(posts1);

        Posts posts2=new Posts();
        posts2.setTitle("love you");
        repository.save(posts2);

        List<Posts> result=repository.findAll();
        Assertions.assertThat(result.size()).isEqualTo(2);

    }

}

