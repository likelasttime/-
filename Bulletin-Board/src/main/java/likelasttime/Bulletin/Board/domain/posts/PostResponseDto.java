package likelasttime.Bulletin.Board.domain.posts;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class PostResponseDto implements Serializable {
    private Long id;
    private String title;
    private String author;
    private String content;

    @JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using= LocalDateTimeDeserializer.class)
    private LocalDateTime createdDate;

    @JsonSerialize(using= LocalDateTimeSerializer.class)
    @JsonDeserialize(using= LocalDateTimeDeserializer.class)
    private LocalDateTime modifiedDate;

    private int view;
    private List<CommentResponseDto> comment;
    private int comment_cnt;

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
        this.comment_cnt=post.getComment_cnt();

    }

}
