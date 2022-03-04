package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.Repository.UserRepository;
import likelasttime.Bulletin.Board.domain.posts.Role;
import likelasttime.Bulletin.Board.domain.posts.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
public class EmailServiceTest {
    @Autowired
    EmailServiceImpl emailService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @AfterEach
    public void afterEach(){
        userRepository.deleteAll();
    }

    @Test
    public void sendMessage() throws Exception{
        // given
        User user1=new User();
        user1.setUsername("spring00");
        user1.setEmail("a50070863@gmail.com");
        user1.setEnabled(true);
        user1.setName("스프링");
        user1.setPassword("lovespring00!");
        user1.setPhone("01012345678");
        user1.setRoles(new ArrayList<Role>());

        // when
        userService.joinUser(user1);
        String password1=user1.getPassword();
        emailService.sendMail(user1.getEmail(), user1.getUsername(), user1);

        // then
        User user2=userRepository.findByUsername("spring00").get();
        assertThat(user2.getPassword()).isNotEqualTo(password1);      // 임시 비밀번호로 변경됨

    }


}
