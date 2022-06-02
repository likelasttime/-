package likelasttime.Bulletin.Board.domain.posts;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;

    private String user_name;

    private Long post_id;

    private String comment;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    @Builder
    public CommentResponseDto(Comment comment) {
        this.id=comment.getId();
        this.comment=comment.getComment();
        this.user_name=comment.getUser().getUsername();
        this.post_id=comment.getPost().getId();
        this.createdDate=comment.getCreatedDate();
        this.modifiedDate=comment.getModifiedDate();
    }

}
