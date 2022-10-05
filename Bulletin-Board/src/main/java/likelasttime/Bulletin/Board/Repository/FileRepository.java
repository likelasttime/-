package likelasttime.Bulletin.Board.Repository;

import likelasttime.Bulletin.Board.domain.posts.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<UploadFile, Long> {

}
