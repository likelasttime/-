package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.Repository.SpringDataJpaPostRepository;
import likelasttime.Bulletin.Board.domain.posts.Post;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import static org.assertj.core.api.Assertions.*;
import java.util.*;


@SpringBootTest
class PostServiceTest {

    PostService postService;
    @Autowired
    SpringDataJpaPostRepository repository;

    @BeforeEach
    public void beforeEach(){
        postService=new PostService(repository);
    }

    @AfterEach
    public void afterEach(){    // 각 테스트 종료될 때마다 실행
        repository.deleteAll();
    }

    @Test
    public void 글_작성(){
        //given
        Post post=new Post();
        post.setTitle("spring");
        post.setAuthor("iu");
        post.setContent("안녕");

        //when
        postService.join(post);

        //then
        Post findPost=repository.findById(post.getId()).get();
        assertThat(post.getTitle()).isEqualTo(findPost.getTitle());
        assertThat(post.getAuthor()).isEqualTo(findPost.getAuthor());
        assertThat(post.getContent()).isEqualTo(findPost.getContent());
    }

    @Test
    public void findAll() {
        //given
        Post post1=new Post();
        post1.setTitle("Spring1");
        repository.save(post1);

        Post post2=new Post();
        post2.setTitle("Spring2");
        repository.save(post2);

        Pageable pageable= PageRequest.of(0,5);

        //when
        Page<Post> result=postService.findPost(pageable);

        //then
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    @Test
    public void findById(){
        // given
        Post post=new Post();
        post.setTitle("Spring");
        repository.save(post);

        // when
        Post result=postService.findOne(post.getId()).get();

        // then
        assertThat(result.getTitle()).isEqualTo("Spring");

    }

    @Test
    public void update(){
        // given
        Post post=new Post();
        post.setTitle("Spring1");
        post.setContent("hello");
        repository.save(post);

        post.setTitle("Spring2");   // 수정
        repository.save(post);

        // when
        Post result=postService.findOne(post.getId()).get();

        // then
        assertThat(result.getTitle()).isEqualTo("Spring2");
        assertThat(result.getContent()).isEqualTo("hello");
        assertThat(result.getView()).isEqualTo(0);

    }

    @Test
    public void delete(){
        // given
        Post post=new Post();
        post.setTitle("Spring");
        repository.save(post);

        // when
        postService.deletePost(post.getId());

        // then
        List<Post> all_post=repository.findAll();
        assertThat(all_post.size()).isEqualTo(0);
    }

    @Test
    public void updateView(){
        // given
        Post post=new Post();
        post.setTitle("spring");
        repository.save(post);

        // when
        postService.updateView(post.getId());

        // then
        Post result=repository.findById(post.getId()).get();
        assertThat(result.getView()).isEqualTo(1);
    }

    @Test
    public void search(){
        // given
        Post post=new Post();
        post.setTitle("hello");
        post.setContent("I like ..");
        post.setAuthor("ju");
        repository.save(post);

        String keyword="ju";
        Pageable pageable= PageRequest.of(0,5);
        Page<Post> page=postService.findPost(pageable);

        // when
        Page<Post> result=postService.search(keyword, keyword, keyword, pageable);

        // then
        assertThat(result.getTotalElements()).isEqualTo(1);

    }
}