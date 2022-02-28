package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.domain.posts.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl {
    private final JavaMailSender emailSender;
    private final UserService userService;
    private final SpringTemplateEngine templateEngine;

    public void sendMail(String address, String username, User user) throws MessagingException {
        HashMap<String, String> map=new HashMap<>();
        String new_password=makePassword();     // 임시 비밀번호 발급

        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        //메일 제목 설정
        helper.setSubject("'Board 관리자' 님이 발송한 임시 비밀번호입니다.");

        //수신자 설정
        helper.setTo(address);

        //템플릿에 전달할 데이터 설정
        final Context context = new Context();
        context.setVariable("name", username);
        context.setVariable("password", new_password);

        map.forEach((key, value)->{ context.setVariable(key, value); });

        //메일 내용 설정 : 템플릿 프로세스
        String html = this.templateEngine.process("newPasswordMail", context);
        helper.setText(html, true);

        // 비밀번호 갱신
        user.setPassword(new_password);
        userService.joinUser(user);

        //메일 보내기
        emailSender.send(message);

    }

    // 임시 비밀번호 생성
    public String makePassword(){
        String pw="";
        for(int i=0; i<12; i++){
            pw+=(char)((Math.random()*26)+97);
        }
        return pw;
    }

}