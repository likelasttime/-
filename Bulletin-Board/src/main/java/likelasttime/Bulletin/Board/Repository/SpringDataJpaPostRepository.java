package likelasttime.Bulletin.Board.Repository;

import likelasttime.Bulletin.Board.domain.posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataJpaPostRepository  extends JpaRepository<Post, Long>, PostRepository {
    @Override
    Optional<Post> findByTitle(String title);
    List<Post> findAll();

}
