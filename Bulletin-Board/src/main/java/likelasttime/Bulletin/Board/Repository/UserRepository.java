package likelasttime.Bulletin.Board.Repository;

import likelasttime.Bulletin.Board.domain.posts.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    List<User> findAll();
    boolean existsByUsername(String user_id);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
    void deleteAll();
    Optional<User> findByNameAndEmailAndPhone(String name, String email, String phone);
    boolean existsByUsernameAndEmail(String username, String mail);
    void delete(User user);
    Optional<User> findByEmail(String email);
}
