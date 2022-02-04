package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.domain.posts.User;
import org.springframework.validation.Errors;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    boolean userIdCheck(String user_id);
    Map<String, String> handling(Errors errors);
    void joinUser(User user);
    boolean userEmailCheck(String email);
    boolean userPhoneCheck(String phone);
    List<User> findAll();
    Optional<User> findById(Long id);
    void save(User user);
    void deleteAll();

}
