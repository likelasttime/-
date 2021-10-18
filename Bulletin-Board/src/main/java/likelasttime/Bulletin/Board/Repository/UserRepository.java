package likelasttime.Bulletin.Board.Repository;

import likelasttime.Bulletin.Board.domain.posts.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
}
