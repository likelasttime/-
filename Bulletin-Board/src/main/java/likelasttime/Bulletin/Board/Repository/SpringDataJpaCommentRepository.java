package likelasttime.Bulletin.Board.Repository;

import likelasttime.Bulletin.Board.domain.posts.Comment;
import likelasttime.Bulletin.Board.domain.posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpringDataJpaCommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findCommentsByPost(Post post);
}
