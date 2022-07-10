package likelasttime.Bulletin.Board.Repository;

import likelasttime.Bulletin.Board.domain.posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SpringDataJpaPostRepository extends JpaRepository<Post, Long>, PostRepository{
    List<Post> findTop10ByOrderByViewDesc();
}
