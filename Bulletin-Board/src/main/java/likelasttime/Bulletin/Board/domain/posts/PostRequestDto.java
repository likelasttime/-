package likelasttime.Bulletin.Board.domain.posts;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class PostRequestDto {
    private Long id;
    private String title;
    private String author;
    private String content;
    private int view;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private List<CommentResponseDto> comments;

}
