package likelasttime.Bulletin.Board.Repository;

import likelasttime.Bulletin.Board.domain.posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataJpaPostRepository  extends JpaRepository<Post, Long>{
    Optional<Post> findByTitle(String title);
    //Optional<Post> findOne(Long id);
    Post findById(Long id);

    void deleteById(Long id);
    //List<Post> findAll();
    //List<Post> findAll();



}
