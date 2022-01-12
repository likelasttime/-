package likelasttime.Bulletin.Board.validator;

import likelasttime.Bulletin.Board.Repository.UserRepository;
import likelasttime.Bulletin.Board.domain.posts.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
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
        if(userRepository.existsByEmail(userRequestDto.getEmail())){    // 이메일 중복
            errors.rejectValue("email", "invalid.email"
                    , new Object[]{userRequestDto.getEmail()}
                    , "이미 사용중인 이메일입니다.");
        }
        if(!Pattern.matches(pattern, userRequestDto.getUsername())){     // 첫 문자가 영문자가 아니면
            errors.rejectValue("username", "invalid.username"
                    , new Object[]{userRequestDto.getUsername()}
                    , "첫 문자는 영문자야 합니다.");
        }
        if(userRepository.existsByUsername(userRequestDto.getUsername())){    // 아이디 중복
            errors.rejectValue("username", "invalid.username"
            , new Object[]{userRequestDto.getUsername()}
            , "이미 사용중인 아이디입니다.");
        }
        if(userRepository.existsByPhone(userRequestDto.getPhone())){  // 연략처 중복
            errors.rejectValue("phone", "invalid.phone"
            , new Object[]{userRequestDto.getPhone()}
            , "이미 가입된 번호입니다.");
        }
    }
}
