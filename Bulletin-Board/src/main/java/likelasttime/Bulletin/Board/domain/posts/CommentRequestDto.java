package likelasttime.Bulletin.Board.domain.posts;

import lombok.*;

@Setter
@NoArgsConstructor
@Getter
public class CommentRequestDto {
    private Long id;

    private String comment;

    private User user;

    private Post post;

    @Builder
    public CommentRequestDto(Long id, String comment, Post post, User user){
        this.id=id;
        this.comment=comment;
        this.post=post;
        this.user=user;
    }

}
