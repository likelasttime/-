package likelasttime.Bulletin.Board.domain.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class UploadFile {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String originFileName;

    @Column
    private String uuidFileName;

    @Column
    private String filePath;

    @Column
    private String contentType;

    @Column
    private Long fileSize;

    @Column
    private LocalDateTime registerDate;
}
