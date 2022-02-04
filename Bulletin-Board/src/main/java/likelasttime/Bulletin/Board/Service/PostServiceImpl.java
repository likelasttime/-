package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.Repository.PostRepository;
import likelasttime.Bulletin.Board.domain.posts.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;


@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private static final int PAGE_NUM_COUNT=5;
    private static final int POST_COUNT=4;  // 페이지당 게시글 수

    // 게시글 작성
    public void create(Post post){
        //validateDuplicatePost(post);  // 중복 게시글
        postRepository.save(post);
    }

    private void validateDuplicatePost(Post post){
        postRepository.findByTitle(post.getTitle())
                .ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 게시글입니다.");
                });
    }

    //전체 게시글 조회
    public List<Post> findAll(){
        return postRepository.findAll();
    }

    // 특정 게시글 조회
    public Optional<Post> findById(Long postId){
        return postRepository.findById(postId);
    }

    // 삭제
    public void deletePost(Long id){
        postRepository.deleteById(id);
    }

    // 조회수 증가
    public void updateView(Long id) {
        Post post=postRepository.findById(id).get();
        post.setView(post.getView()+1);
        postRepository.save(post);
    }

    // 검색
    public Page<Post> search(String title, String content, String author, Pageable pageable){
        Page<Post> lst=postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrAuthorContainingIgnoreCase(title, content, author, pageable);
        return lst;
    }

    public void deleteAll(){
        postRepository.deleteAll();
    }
}
