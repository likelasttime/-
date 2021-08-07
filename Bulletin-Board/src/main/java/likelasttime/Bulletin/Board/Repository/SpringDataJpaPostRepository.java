package likelasttime.Bulletin.Board.Repository;

import likelasttime.Bulletin.Board.domain.posts.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SpringDataJpaPostRepository  extends JpaRepository<Post, Long>{

    Optional<Post> findByTitle(String title);
    Optional<Post> findById(Long id);
    void deleteById(Long id);
    //Page<Post> findAllByOrderByFirstCreatedDateDesc(Pageable pageable);
    Page<Post> findAll(Pageable pageable);
    Page<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String content, String author, Pageable pageable);
    @Modifying
    @Query("UPDATE Post p SET view=view+1 WHERE p.id=:id")
    int updateView(@Param("id") Long id);

}
