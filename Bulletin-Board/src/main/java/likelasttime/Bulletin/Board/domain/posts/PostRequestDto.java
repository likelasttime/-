package likelasttime.Bulletin.Board.domain.posts;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class PostRequestDto {
    private Long id;

    @NotBlank(message="제목을 작성해주세요.")
    @Size(max=30, message="제목은 30자 이하입니다.")
    private String title;

    private String author;

    @NotBlank(message="내용을 작성해주세요.")
    @Size(max=10000, message="10,000자 이하로 작성하세요.")
    private String content;

    private int view;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private int comment_cnt;

    private Long fileId;

}
