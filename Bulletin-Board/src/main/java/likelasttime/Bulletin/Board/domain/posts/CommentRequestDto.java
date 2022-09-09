package likelasttime.Bulletin.Board.domain.posts;

import lombok.*;

@Setter
@NoArgsConstructor
@Getter
public class CommentRequestDto {
    private Long id;

    private String content;

    private Long postId;

    private User user;

    private Post post;

    @Builder
    public CommentRequestDto(String content, Long postId, User user, Post post){
        this.content=content;
        this.postId=postId;
        this.user=user;
        this.post=post;
    }

}
