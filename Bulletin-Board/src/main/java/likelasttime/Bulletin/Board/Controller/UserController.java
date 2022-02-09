package likelasttime.Bulletin.Board.Controller;

import likelasttime.Bulletin.Board.Service.UserServiceImpl;
import likelasttime.Bulletin.Board.domain.posts.User;
import likelasttime.Bulletin.Board.domain.posts.UserRequestDto;
import likelasttime.Bulletin.Board.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Map;


@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userService;
    private final UserValidator userValidator;
    private final static Logger LOG = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/user/joinForm")
    public String joinForm(UserRequestDto userDto){
        return "user/joinForm";
    }

    @PostMapping("/user/idCheck")       // 아이디 중복 체크
    @ResponseBody
    public boolean idCheck(@RequestParam("id") String id){
        LOG.info("userIdCheck 진입");
        LOG.info("전달받은 id:"+id);
        boolean flag=userService.userIdCheck(id);
        LOG.info("확인 결과:" + flag);
        return flag;
    }

    @PostMapping("/user/emailCheck")        // 이메일 중복 체크
    @ResponseBody
    public boolean emailCheck(@RequestParam("email") String email){
        LOG.info("userEmailCheck 진입");
        LOG.info("전달받은 email:"+email);
        boolean flag=userService.userEmailCheck(email);
        LOG.info("확인 결과:"+flag);
        return flag;
    }

    @PostMapping("/user/phoneCheck")    // 전화번호 중복 검사
    @ResponseBody
    public boolean phoneCheck(@RequestParam("phone") String phone){
        LOG.info("userPhoneCheck 진입");
        LOG.info("전달받은 번호:"+phone);
        boolean flag=userService.userPhoneCheck(phone);
        LOG.info("확인 결과:"+flag);
        return flag;
    }

    @PostMapping("/user/join")     // 회원가입
    public String join(@Valid UserRequestDto userRequestDto, Errors errors, Model model){
        userValidator.validate(userRequestDto, errors);
        if(errors.hasErrors()) {     // 회원가입 실패 : 비밀번호만 지워진다.
            model.addAttribute("userDto", userRequestDto);
            Map<String, String> result = userService.handling(errors);
            for (String key : result.keySet()) {
                model.addAttribute(key, result.get(key));
            }
            LOG.info("회원가입 실패");
            return "user/joinForm";
        }
        User user=userRequestDto.toEntity();
        userService.joinUser(user);
        LOG.info("회원가입 성공");
        return "redirect:/user/login";
    }

    @GetMapping("/user/list")
    public String list(Model model){
        model.addAttribute("users", userService.findAll());
        return "/user/list";
    }

    @GetMapping("/user/form")      // 개인정보 수정
    public String updateForm(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String id=authentication.getName(); // 로그인한 유저 id
        model.addAttribute("userDto", userService.findByUsername(id).get());
        return "/user/updateForm";
    }

    @PutMapping("/user/{id}")     // 개인정보 수정
    public String update(@Valid UserRequestDto userRequestDto, Errors errors, Model model, @PathVariable Long id){
        // 변경사항이 있나?
        if(!userService.checkUpdate(userRequestDto, id)){
            LOG.info("개인정보 수정 변동 사항 없음");
            return "redirect:/";
        }
        userValidator.validate(userRequestDto, errors);
        if(errors.hasErrors()){     // 유효성 검사 실패
            model.addAttribute("userDto", userRequestDto);
            Map<String, String> result=userService.handling(errors);
            for(String key : result.keySet()){
                model.addAttribute(key, result.get(key));
            }
            LOG.info("개인정보 수정 실패");
            return "user/updateForm";
        }
        User user=userRequestDto.toEntity();
        userService.joinUser(user);
        LOG.info("개인정보 수정 성공");

        return "redirect:/";
    }

    @GetMapping("/user/login")
    public String loginForm(){
        return "/user/login";
    }

}
