package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.Repository.*;
import likelasttime.Bulletin.Board.domain.posts.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class CommentService {
    private final SpringDataJpaCommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public Long commentSave(String name, Long id, String content){
        User user=userRepository.findByUsername(name).get();
        Post post=postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("댓글 쓰기 실패: 해당 게시글이 존재하지 않습니다." + id));
        post.update(post.getTitle(), post.getContent(), post.getView(), post.getComment_cnt() + 1);
        CommentRequestDto comment=CommentRequestDto.builder()
                .comment(content)
                .post(post)
                .user(user)
                .build();

        Comment comment_entity=modelMapper.map(comment, Comment.class);
        commentRepository.save(comment_entity);

        return comment_entity.getId();
    }

    public void update(Long id, String content) {
        Comment comment=commentRepository.findById(id).get();
        comment.update(content);
    }

    public void delete(Long id, Long post_id) {
        Post post=postRepository.findById(post_id).get();
        post.update(post.getTitle(), post.getContent(), post.getView(), post.getComment_cnt() - 1);
        commentRepository.deleteById(id);
    }

    public void deleteAll(){commentRepository.deleteAll();}

    public Comment findById(Long id){return commentRepository.findById(id).get();}

    public List<Comment> findAll(){return commentRepository.findAll();}

}
