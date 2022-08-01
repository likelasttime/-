package likelasttime.Bulletin.Board.domain.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileDto {
    private Long id;
    private String originFileName;
    private String fileName;
    private String filePath;
}
