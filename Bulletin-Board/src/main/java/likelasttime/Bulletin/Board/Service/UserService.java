package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.domain.posts.User;
import likelasttime.Bulletin.Board.domain.posts.UserRequestDto;
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
    Optional<User> findByUsername(String username);
    void save(User user);
    void deleteAll();
    boolean checkUpdate(UserRequestDto userRequestDto, Long id);
    Optional<User> findUserId(User user);
    boolean findPassword(String username, String mail);
    String getUserId();
    void deleteUser(Long id);

}
