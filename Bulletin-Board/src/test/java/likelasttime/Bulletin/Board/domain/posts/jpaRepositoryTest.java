package likelasttime.Bulletin.Board.domain.posts;

import likelasttime.Bulletin.Board.Repository.SpringDataJpaPostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;
import static org.assertj.core.api.Assertions.*;


@SpringBootTest
public class jpaRepositoryTest {

    @Autowired
    SpringDataJpaPostRepository jpaRepository;

    @AfterEach
    public void afterEach(){jpaRepository.deleteAll();}

    @Test
    public void save(){
        // given
        Post post=new Post();
        post.setTitle("Spring");

        // when
        jpaRepository.save(post);

        // then
        Post result=jpaRepository.findByTitle(post.getTitle()).get();
        assertThat(result.getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    public void findById(){
        // given
        Post post=new Post();
        post.setTitle("spring");
        jpaRepository.save(post);

        // when
        Post result=jpaRepository.findById(post.getId()).get();

        // then
        assertThat(result.getTitle()).isEqualTo(post.getTitle());
    }

    @Test
    public void findByTitle(){
        // given
        Post post1=new Post();
        post1.setTitle("Spring1");
        jpaRepository.save(post1);

        Post post2=new Post();
        post2.setTitle("Spring2");
        jpaRepository.save(post2);

        // when
        Post result=jpaRepository.findByTitle("Spring1").get();

        // then
        assertThat(result.getId()).isEqualTo(post1.getId());
    }

    @Test
    public void findAll(){
        // given
        Post post1=new Post();
        post1.setTitle("spring1");
        jpaRepository.save(post1);

        Post post2=new Post();
        post2.setTitle("spring2");
        jpaRepository.save(post2);

        // when
        List<Post> result=jpaRepository.findAll();

        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void deleteOne(){
        // given
        Post post=new Post();
        post.setTitle("Spring");
        jpaRepository.save(post);

        // when
        jpaRepository.deleteById(post.getId());

        // then
        assertThat(jpaRepository.findByTitle("Spring").empty());
    }

}
