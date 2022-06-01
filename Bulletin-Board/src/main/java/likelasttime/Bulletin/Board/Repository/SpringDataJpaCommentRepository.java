package likelasttime.Bulletin.Board.Repository;

import likelasttime.Bulletin.Board.domain.posts.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaCommentRepository extends JpaRepository<Comment, Long> {
}
