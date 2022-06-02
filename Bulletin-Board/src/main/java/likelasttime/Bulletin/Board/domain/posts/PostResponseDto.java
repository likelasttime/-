package likelasttime.Bulletin.Board.domain.posts;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String author;
    private String content;
    private LocalDateTime createdDate, modifiedDate;
    private int view;
    private List<CommentResponseDto> comment;

    @Builder
    public PostResponseDto(Post post) {
        this.id=post.getId();
        this.title=post.getTitle();
        this.author=post.getAuthor();
        this.content=post.getContent();
        this.createdDate=post.getCreatedDate();
        this.modifiedDate=post.getModifiedDate();
        this.view=post.getView();
        this.comment=post.getComment().stream().map(CommentResponseDto::new).collect(Collectors.toList());

    }


}
