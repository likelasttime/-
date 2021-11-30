package likelasttime.Bulletin.Board.Repository;

import likelasttime.Bulletin.Board.domain.posts.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SpringDataJpaPostRepository  extends JpaRepository<Post, Long>{

    Optional<Post> findByTitle(String title);
    Optional<Post> findById(Long id);
    void deleteById(Long id);
    //Page<Post> findAllByOrderByFirstCreatedDateDesc(Pageable pageable);
    Page<Post> findAll(Pageable pageable);
    Page<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String content, String author, Pageable pageable);
}
