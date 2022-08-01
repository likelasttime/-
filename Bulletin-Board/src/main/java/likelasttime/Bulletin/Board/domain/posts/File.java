package likelasttime.Bulletin.Board.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor
public class File {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable=false)
    private String originFileName;

    @Column(nullable=false)
    private String fileName;

    @Column(nullable=false)
    private String filePath;

    @Builder
    public File(Long id, String originFileName, String fileName, String filePath){
        this.id=id;
        this.originFileName=originFileName;
        this.fileName=fileName;
        this.filePath=filePath;
    }
}
