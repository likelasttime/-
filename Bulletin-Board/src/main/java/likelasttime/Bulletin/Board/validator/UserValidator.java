package likelasttime.Bulletin.Board.validator;

import likelasttime.Bulletin.Board.Repository.UserRepository;
import likelasttime.Bulletin.Board.domain.posts.User;
import likelasttime.Bulletin.Board.domain.posts.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator {
    private final UserRepository userRepository;
    String pattern="^[a-zA-Z].+$";

    @Override
    public boolean supports(Class<?> aClass){
        return aClass.isAssignableFrom(UserRequestDto.class);
    }

    @Override
    public void validate(Object object, Errors errors){
        UserRequestDto userRequestDto=(UserRequestDto) object;
        Optional<User> user=userRepository.findByUsername(userRequestDto.getUsername());

        String email=userRequestDto.getEmail();
        String username=userRequestDto.getUsername();
        String phone=userRequestDto.getPhone();

        if(userRepository.existsByEmail(email)){    // 이메일 중복
            if((user.isPresent() && !email.equals(user.get().getEmail())) || !user.isPresent()) {    // 수정한 이메일 또는 가입할 이메일
                errors.rejectValue("email", "invalid.email"
                        , new Object[]{email}
                        , "이미 사용중인 이메일입니다.");
            }
        }

        if(!Pattern.matches(pattern, username)){     // 첫 문자가 영문자가 아니면
            errors.rejectValue("username", "invalid.username"
                    , new Object[]{username}
                    , "첫 문자는 영문자야 합니다.");
        }

        if(userRepository.existsByUsername(username)){    // 아이디 중복
            if((user.isPresent() && !username.equals(user.get().getUsername())) || !user.isPresent()) {    // 수정할 아이디 또는 가입할 아이디
                errors.rejectValue("username", "invalid.username"
                        , new Object[]{username}
                        , "이미 사용중인 아이디입니다.");
            }
        }

        if(userRepository.existsByPhone(phone)){  // 연략처 중복
            if((user.isPresent() && !phone.equals(user.get().getPhone())) || !user.isPresent()) {    // 수정한 연락처 또는 가입할 연락처
                errors.rejectValue("phone", "invalid.phone"
                        , new Object[]{phone}
                        , "이미 가입된 번호입니다.");
            }
        }
    }
}
