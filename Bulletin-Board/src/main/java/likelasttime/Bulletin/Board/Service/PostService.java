package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.domain.posts.Post;
import likelasttime.Bulletin.Board.domain.posts.PostRequestDto;
import likelasttime.Bulletin.Board.domain.posts.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface PostService {
    PostResponseDto create(PostRequestDto post);

    PostResponseDto update(Long id, PostRequestDto post);

    Optional<Post> findById(Long postId) throws IOException;

    void deletePost(Long id);

    void updateView(Long id);

    Page<PostResponseDto> search(String title, String content, String author, Pageable pageable);

    List<PostResponseDto> findAll();

    List<PostResponseDto> findByRank();

    void deleteAll();
}


