package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.Repository.UserRepository;
import likelasttime.Bulletin.Board.domain.posts.Role;
import likelasttime.Bulletin.Board.domain.posts.User;
import likelasttime.Bulletin.Board.domain.posts.UserRequestDto;
import likelasttime.Bulletin.Board.domain.posts.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // 중복 아이디 체크
    @Override
    public boolean userIdCheck(String user_id) {
        return userRepository.existsByUsername(user_id);
    }

    @Override
    public Map<String, String> handling(Errors errors){
        Map<String, String> result=new HashMap<>();
        for(FieldError error : errors.getFieldErrors()){
            String key=String.format("valid_%s", error.getField());
            result.put(key, error.getDefaultMessage());
        }
        return result;
    }

    @Override
    public void joinUser(User user){
        String encodedPassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setEnabled(true);
        Role role=new Role();
        role.setId(1l);
        user.getRoles().add(role);
        userRepository.save(user);
    }

    // 중복 이메일 체크
    @Override
    public boolean userEmailCheck(String email){return userRepository.existsByEmail(email);}

    // 중복 휴대폰 번호 체크
    @Override
    public boolean userPhoneCheck(String phone){return userRepository.existsByPhone(phone);}

    @Override
    public List<User> findAll(){return userRepository.findAll();}

    @Override
    public Optional<User> findById(Long id){return userRepository.findById(id);}

    @Override
    public UserResponseDto findByUsername(String username){
        User user = userRepository.findByUsername(username).orElse(new User());
        UserResponseDto userResponseDto = modelMapper.map(user, UserResponseDto.class);
        return userResponseDto;
    }

    @Override
    public void save(User user){userRepository.save(user);}

    @Override
    public void deleteAll(){ userRepository.deleteAll(); }

    @Override
    public boolean checkUpdate(UserRequestDto userRequestDto, Long id){
        // 데이터베이스에 저장된 정보와 비교
        User beforeUser=userRepository.findById(id).get();
        User afterUser=userRequestDto.toEntity();

        if(!afterUser.getName().equals(beforeUser.getName())) {
            return true;
        }else if(!passwordEncoder.matches(afterUser.getPassword(), beforeUser.getPassword())){
            return true;
        }else if(!afterUser.getEmail().equals(beforeUser.getEmail())){
            return true;
        }else if(!afterUser.getPhone().equals(beforeUser.getPhone())){
            return true;
        }
        return false;        // 수정 사항 없음
    }

    @Override
    public Optional<User> findUserId(User user){
        String name=user.getName();
        String email=user.getEmail();
        String phone=user.getPhone();

        Optional<User> result=userRepository.findByNameAndEmailAndPhone(name, email, phone);

        return result;
    }

    @Override
    public boolean findPassword(String username, String mail){
        return userRepository.existsByUsernameAndEmail(username, mail);
    }

    @Override
    public String getUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id=authentication.getName(); // 로그인한 유저 id
        return id;
    }

    @Override
    public void deleteUser(String id){
        Optional<User> user=userRepository.findByUsername(id);
        user.ifPresent(u -> {
            userRepository.delete(u);
        });
    }

    @Override
    public void logout(HttpSession session){
        session.removeAttribute("user");  // 로그아웃
        session.invalidate();
    }
}
