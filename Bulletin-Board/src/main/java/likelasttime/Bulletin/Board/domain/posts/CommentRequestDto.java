package likelasttime.Bulletin.Board.domain.posts;

import lombok.*;

@Setter
@NoArgsConstructor
@Getter
public class CommentRequestDto {
    private Long id;

    private String comment;

    private Long postId;

    private User user;

    private Post post;

    @Builder
    public CommentRequestDto(String comment, Long postId, User user, Post post){
        this.comment=comment;
        this.postId=postId;
        this.user=user;
        this.post=post;
    }

}
