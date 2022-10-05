package likelasttime.Bulletin.Board.Repository;

import likelasttime.Bulletin.Board.domain.posts.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataJpaUserRepository extends JpaRepository<User, Long>, UserRepository {

}
