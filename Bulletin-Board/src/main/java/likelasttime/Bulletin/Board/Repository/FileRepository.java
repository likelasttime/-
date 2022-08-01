package likelasttime.Bulletin.Board.Repository;

import likelasttime.Bulletin.Board.domain.posts.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {

}
