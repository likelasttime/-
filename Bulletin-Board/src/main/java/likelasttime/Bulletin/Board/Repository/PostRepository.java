package likelasttime.Bulletin.Board.Repository;

import likelasttime.Bulletin.Board.domain.posts.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository{
    void save(Post post);
    Optional<Post> findById(Long id);
    Optional<Post> findByTitle(String title);
    List<Post> findAll();
    void update(Long id, Post post);
    void delete(Long id);
}