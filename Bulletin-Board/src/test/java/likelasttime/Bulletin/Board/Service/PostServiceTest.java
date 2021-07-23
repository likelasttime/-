package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.Repository.MemoryPostRepository;
import likelasttime.Bulletin.Board.Repository.SpringDataJpaPostRepository;
import likelasttime.Bulletin.Board.domain.posts.Post;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class PostServiceTest {

    PostService postService;
    MemoryPostRepository postRepository;

    @BeforeEach
    public void beforeEach(){
        postRepository=new MemoryPostRepository();
        postService=new PostService(postRepository);
    }

    @AfterEach
    public void afterEach(){
        postRepository.clearBlog();
    }

    @Test
    public void 회원가입() throws Exception{
        //given
        Post post=new Post();
        post.setTitle("spring");

        //when
        Long saveId=postService.join(post);

        //then
        Post findPost=postRepository.findById(saveId).get();
        assertThat(post.getTitle()).isEqualTo(findPost.getTitle());
    }

    @Test
    public void 중복_회원_예외() throws Exception{
        //given
        Post post1=new Post();
        post1.setTitle("spring1");

        Post post2=new Post();
        post2.setTitle("spring1");

        //when
        postService.join(post1);
        IllegalStateException e=assertThrows(IllegalStateException.class, ()->postService.join(post2));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 게시글입니다.");
    }

}