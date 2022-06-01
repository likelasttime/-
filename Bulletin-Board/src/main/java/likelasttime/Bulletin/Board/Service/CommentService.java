package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.Repository.*;
import likelasttime.Bulletin.Board.domain.posts.Comment;
import likelasttime.Bulletin.Board.domain.posts.CommentRequestDto;
import likelasttime.Bulletin.Board.domain.posts.Post;
import likelasttime.Bulletin.Board.domain.posts.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        CommentRequestDto comment=CommentRequestDto.builder()
                .comment(content)
                .post(post)
                .user(user)
                .build();

        Comment comment_entity=modelMapper.map(comment, Comment.class);
        commentRepository.save(comment_entity);

        return comment_entity.getId();
    }
}
