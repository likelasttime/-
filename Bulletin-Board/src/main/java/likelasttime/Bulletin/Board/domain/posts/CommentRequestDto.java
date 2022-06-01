package likelasttime.Bulletin.Board.domain.posts;

import lombok.*;

@Setter
@NoArgsConstructor
@Getter
public class CommentRequestDto {
    private String comment;

    private User user;

    private Post post;

    @Builder
    public CommentRequestDto(String comment, Post post, User user){
        this.comment=comment;
        this.post=post;
        this.user=user;
    }

}
