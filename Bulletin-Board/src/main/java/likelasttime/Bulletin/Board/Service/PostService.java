package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.domain.posts.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface PostService {
    void create(Post post);

    Optional<Post> findById(Long postId);

    void deletePost(Long id);

    void updateView(Long id);

    Page<Post> search(String title, String content, String author, Pageable pageable);

    List<Post> findAll();

    void deleteAll();
}


