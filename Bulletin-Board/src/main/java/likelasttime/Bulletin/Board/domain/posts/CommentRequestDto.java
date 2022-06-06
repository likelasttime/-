package likelasttime.Bulletin.Board.domain.posts;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@NoArgsConstructor
@Getter
public class CommentRequestDto {
    private Long id;

    @NotBlank(message="댓글을 작성하세요.")
    @Size(max=10000, message="10,000자 이하로 작성하세요.")
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
