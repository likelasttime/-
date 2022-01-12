package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.Repository.UserRepository;
import likelasttime.Bulletin.Board.domain.posts.Role;
import likelasttime.Bulletin.Board.domain.posts.User;
import likelasttime.Bulletin.Board.domain.posts.UserRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
    }

    // 중복 아이디 체크
    public boolean userIdCheck(String user_id) {
        return userRepository.existsByUsername(user_id);
    }

    public Map<String, String> handling(Errors errors){
        Map<String, String> result=new HashMap<>();
        for(FieldError error : errors.getFieldErrors()){
            String key=String.format("valid_%s", error.getField());
            result.put(key, error.getDefaultMessage());
        }
        return result;
    }

    public void joinUser(UserRequestDto userRequestDto){
        String encodedPassword=passwordEncoder.encode(userRequestDto.getPassword());
        userRequestDto.setPassword(encodedPassword);
        userRequestDto.setEnabled(true);
        Role role=new Role();
        role.setId(1l);
        userRequestDto.getRoles().add(role);
        userRepository.save(userRequestDto.toEntity());
    }

    // 중복 이메일 체크
    public boolean userEmailCheck(String email){return userRepository.existsByEmail(email);}

    // 중복 휴대폰 번호 체크
    public boolean userPhoneCheck(String phone){return userRepository.existsByPhone(phone);}

    public List<User> findAll(){return userRepository.findAll();}

    public Optional<User> findById(Long id){return userRepository.findById(id);}

    public void save(User user){userRepository.save(user);}

}
