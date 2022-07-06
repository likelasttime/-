package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.Repository.PostRepository;
import likelasttime.Bulletin.Board.domain.posts.Post;
import likelasttime.Bulletin.Board.domain.posts.PostRequestDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;


@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private static final int PAGE_NUM_COUNT=5;
    private static final int POST_COUNT=4;  // 페이지당 게시글 수

    // 게시글 작성
    public Post create(PostRequestDto post){
        //validateDuplicatePost(post);  // 중복 게시글
        Post post_entity=modelMapper.map(post, Post.class);
        return postRepository.save(post_entity);
    }

    private void validateDuplicatePost(Post post){
        postRepository.findByTitle(post.getTitle())
                .ifPresent(m->{
                    throw new IllegalStateException("이미 존재하는 게시글입니다.");
                });
    }

    // 게시글 수정
    public Post update(Long id, PostRequestDto post){
        Post post_entity=postRepository.findById(id).get();
        post_entity.update(post.getTitle(), post.getContent(), post_entity.getView(), post_entity.getComment_cnt());
        return post_entity;
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
        post.update(post.getTitle(), post.getContent(), post.getView()+1, post.getComment_cnt());
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
