package likelasttime.Bulletin.Board.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class FileDto {
    private Long id;

    private String originFileName;

    private String uuidFileName;

    private String filePath;

    private String contentType;

    private Long fileSize;

    private LocalDateTime registerDate;

    @Builder
    public FileDto(String originFileName, String filePath, Long fileSize, String uuidFileName, String contentType, LocalDateTime registerDate){
        this.originFileName=originFileName;
        this.filePath=filePath;
        this.fileSize=fileSize;
        this.contentType=contentType;
        this.registerDate=registerDate;
        this.uuidFileName=uuidFileName;
    }
}
