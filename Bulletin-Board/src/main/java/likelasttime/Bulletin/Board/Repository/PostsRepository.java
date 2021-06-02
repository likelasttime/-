package likelasttime.Bulletin.Board.Repository;

import likelasttime.Bulletin.Board.domain.posts.Posts;

import java.util.List;
import java.util.Optional;

public interface PostsRepository{
    Posts save(Posts posts);
    Optional<Posts> findById(Long id);
    Optional<Posts> findByTitle(String title);
    List<Posts> findAll();
}
