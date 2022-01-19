package likelasttime.Bulletin.Board.domain.posts;

import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserRequestDto {
    private Long id;

    @NotBlank(message="이름을 작성해주세요.")
    @Pattern(regexp="^[가-힣]{2,5}$", message="2~5자의 한글만 입력가능합니다.")
    private String name;

    @NotBlank(message="전화번호를 작성해주세요.")
    @Pattern(regexp="^[0-9]{10,11}$", message="10~11자리의 숫자만 입력가능합니다.")
    private String phone;

    @NotBlank(message="메일을 작성해주세요.")
    @Email(message="메일의 양식을 지켜주세요.")
    @Pattern(regexp="^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\\.[A-Za-z0-9\\-]+$"
    , message="예시) 메일명@gmail.com")
    private String email;

    @NotBlank(message="아이디를 입력해주세요.")
    @Pattern(regexp="^(?=.*[0-9]+)[a-zA-Z][a-zA-Z0-9]{5,10}$",
            message="소문자/대문자, 숫자가 포함된 5~10자의 아이디여야 합니다.")
    private String username;

    @NotBlank(message="비밀번호를 입력해주세요.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*\\W)(?=\\S+$).{8,20}",
    message="소문자, 숫자, 특수문자가 모두 포함된 8~20자의 비밀번호여야 합니다.")
    private String password;

    private Boolean enabled;

    private List<Role> roles=new ArrayList<>();

    public User toEntity(){
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .name(name)
                .username(username)
                .phone(phone)
                .enabled(enabled)
                .roles(roles)
                .build();
    }
}
