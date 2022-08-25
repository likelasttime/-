package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.domain.posts.PostRequestDto;
import likelasttime.Bulletin.Board.domain.posts.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostService {
    PostResponseDto create(PostRequestDto postRequestDto);

    PostResponseDto update(Long id, PostRequestDto post);

    PostResponseDto findById(Long postId);

    void deletePost(Long id);

    Page<PostResponseDto> search(String title, String content, String author, Pageable pageable);

    List<PostResponseDto> findAll();

    List<PostResponseDto> findByRank();

    void deleteAll();
}


