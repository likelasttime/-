package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.Repository.PostRepository;
import likelasttime.Bulletin.Board.domain.posts.Post;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    //회원가입
    public Long join(Post post){
        validateDuplicatePosts(post);  // 중복 게시글
        postRepository.save(post);
        return post.getId();
    }

    private void validateDuplicatePosts(Post post){
        postRepository.findByTitle(post.getTitle())
                .ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 게시글입니다.");
                });
    }

    //전체 회원 조회
    public List<Post> findPosts(){
        return postRepository.findAll();
    }

    public Optional<Post> findOne(Long postsId){
        return postRepository.findById(postsId);
    }
}
