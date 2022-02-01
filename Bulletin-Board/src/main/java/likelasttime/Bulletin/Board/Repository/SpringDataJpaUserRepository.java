package likelasttime.Bulletin.Board.Repository;

import likelasttime.Bulletin.Board.domain.posts.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaUserRepository extends JpaRepository<User, Long>, UserRepository{
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    Optional<User> findById(Long id);
    User save(User user);
}
