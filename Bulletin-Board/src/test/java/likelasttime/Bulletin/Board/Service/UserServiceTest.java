package likelasttime.Bulletin.Board.Service;

import likelasttime.Bulletin.Board.domain.posts.Role;
import likelasttime.Bulletin.Board.domain.posts.User;
import likelasttime.Bulletin.Board.domain.posts.UserRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService userService;

    @AfterEach
    public void afterEach(){
        userService.deleteAll();
    }

    @Test
    public void userIdCheck(){
        //given
        User user1=new User();
        user1.setUsername("spring00");
        user1.setEmail("abc@naver.com");
        user1.setEnabled(true);
        user1.setName("스프링");
        user1.setPassword("lovespring00!");
        user1.setPhone("01012345678");
        user1.setRoles(new ArrayList<Role>());

        User user2=new User();
        user2.setUsername("spring00");
        user2.setEmail("abc@naver.com");
        user2.setEnabled(true);
        user2.setName("스프링");
        user2.setPassword("lovespring00!");
        user2.setPhone("01012345678");
        user2.setRoles(new ArrayList<Role>());

        // when
        userService.joinUser(user1);

        // then
        if(userService.userIdCheck(user2.getUsername()));       // 중복된 아이디
    }

    @Test
    public void userPhoneCheck(){
        //given
        User user1=new User();
        user1.setUsername("spring00");
        user1.setEmail("abc@naver.com");
        user1.setEnabled(true);
        user1.setName("스프링");
        user1.setPassword("lovespring00!");
        user1.setPhone("01012345678");
        user1.setRoles(new ArrayList<Role>());

        User user2=new User();
        user2.setUsername("spring00");
        user2.setEmail("abc@naver.com");
        user2.setEnabled(true);
        user2.setName("스프링");
        user2.setPassword("lovespring00!");
        user2.setPhone("01012345678");
        user2.setRoles(new ArrayList<Role>());

        // when
        userService.joinUser(user1);

        // then
        assertTrue(userService.userPhoneCheck(user2.getPhone()));       // 연락처 중복
    }

    @Test
    public void userEmailCheck(){
        //given
        User user1=new User();
        user1.setUsername("spring00");
        user1.setEmail("abc@naver.com");
        user1.setEnabled(true);
        user1.setName("스프링");
        user1.setPassword("lovespring00!");
        user1.setPhone("01012345678");
        user1.setRoles(new ArrayList<Role>());

        User user2=new User();
        user2.setUsername("spring00");
        user2.setEmail("abc@naver.com");
        user2.setEnabled(true);
        user2.setName("스프링");
        user2.setPassword("lovespring00!");
        user2.setPhone("01012345678");
        user2.setRoles(new ArrayList<Role>());

        // when
        userService.joinUser(user1);

        // then
        assertTrue(userService.userEmailCheck(user2.getEmail()));       // 이메일 중복
    }

    @Test
    public void joinUser(){
        //given
        User user1=new User();
        user1.setUsername("spring00");
        user1.setEmail("abc@naver.com");
        user1.setEnabled(true);
        user1.setName("스프링");
        user1.setPassword("lovespring00!");
        user1.setPhone("01012345678");
        user1.setRoles(new ArrayList<Role>());

        // when
        userService.joinUser(user1);

        // then
        User findUser=userService.findById(user1.getId()).get();
        assertThat(user1.getName()).isEqualTo(findUser.getName());
        assertThat(user1.getUsername()).isEqualTo(findUser.getUsername());
        assertThat(user1.getPassword()).isEqualTo(findUser.getPassword());
        assertThat(user1.getEmail()).isEqualTo(findUser.getEmail());
        assertThat(user1.getPhone()).isEqualTo(findUser.getPhone());
    }

    @Test
    public void findAll(){
        // given
        User user1=new User();
        user1.setUsername("spring00");
        user1.setEmail("abc@naver.com");
        user1.setEnabled(true);
        user1.setName("스프링");
        user1.setPassword("lovespring00!");
        user1.setPhone("01012345678");
        user1.setRoles(new ArrayList<Role>());

        User user2=new User();
        user2.setUsername("spring00");
        user2.setEmail("abc@naver.com");
        user2.setEnabled(true);
        user2.setName("스프링");
        user2.setPassword("lovespring00!");
        user2.setPhone("01012345678");
        user2.setRoles(new ArrayList<Role>());

        // when
        userService.joinUser(user1);
        userService.joinUser(user2);
        List<User> userList=userService.findAll();

        // then
        assertThat(userList.size()).isEqualTo(2);
    }

    @Test
    public void findById(){
        // given
        User user1=new User();
        user1.setUsername("spring00");
        user1.setEmail("abc@naver.com");
        user1.setEnabled(true);
        user1.setName("스프링");
        user1.setPassword("lovespring00!");
        user1.setPhone("01012345678");
        user1.setRoles(new ArrayList<Role>());

        // when
        userService.joinUser(user1);
        Optional<User> findUser=userService.findById(user1.getId());

        // then
        assertThat(findUser.get().getId()).isEqualTo(user1.getId());
    }

    @Test
    public void findByUsername(){
        // given
        User user1=new User();
        user1.setUsername("spring00");
        user1.setEmail("abc@naver.com");
        user1.setEnabled(true);
        user1.setName("스프링");
        user1.setPassword("lovespring00!");
        user1.setPhone("01012345678");
        user1.setRoles(new ArrayList<Role>());

        // when
        userService.joinUser(user1);
        User user2=userService.findByUsername(user1.getUsername()).get();

        // then
        assertThat(user2.getUsername()).isEqualTo(user1.getUsername());
    }

    @Test
    public void deleteAll(){
        // given
        User user1=new User();
        user1.setUsername("spring00");
        user1.setEmail("abc@naver.com");
        user1.setEnabled(true);
        user1.setName("스프링");
        user1.setPassword("lovespring00!");
        user1.setPhone("01012345678");
        user1.setRoles(new ArrayList<Role>());

        // when
        userService.joinUser(user1);
        userService.deleteAll();

        // then
        assertThat(userService.findAll().size()).isEqualTo(0);
    }

    @Test
    public void checkUpdate(){
        // given
        UserRequestDto user1=new UserRequestDto();
        user1.setUsername("spring00");
        user1.setEmail("abc@naver.com");
        user1.setEnabled(true);
        user1.setName("스프링");
        user1.setPassword("lovespring00!");
        user1.setPhone("01012345678");
        user1.setRoles(new ArrayList<Role>());

        User user2=user1.toEntity();

        // when
        userService.joinUser(user2);
        user1.setEmail("a@naver.com");
        boolean flag=userService.checkUpdate(user1, user2.getId());

        // then
        assertThat(flag).isEqualTo(true);
    }

    @Test
    public void findUserId(){
        // given
        User user1=new User();
        user1.setUsername("spring00");
        user1.setEmail("abc@naver.com");
        user1.setEnabled(true);
        user1.setName("스프링");
        user1.setPassword("lovespring00!");
        user1.setPhone("01012345678");
        user1.setRoles(new ArrayList<Role>());

        User user2=new User();
        user2.setUsername("spring2");
        user2.setEmail("a@naver.com");
        user2.setEnabled(true);
        user2.setName("스프링");
        user2.setPassword("lovespring00!");
        user2.setPhone("01012345656");
        user2.setRoles(new ArrayList<Role>());

        // when
        userService.joinUser(user1);
        Optional<User> result1=userService.findUserId(user1);
        Optional<User> result2=userService.findUserId(user2);       // 가입하지 않은 사용자

        // then
        assertTrue(result1.isPresent());        // 성공
        assertFalse(result2.isPresent());      // 실패

    }

    @Test
    public void deleteUser(){
        // given
        User user1=new User();
        user1.setUsername("spring00");
        user1.setEmail("abc@naver.com");
        user1.setEnabled(true);
        user1.setName("스프링");
        user1.setPassword("lovespring00!");
        user1.setPhone("01012345678");
        user1.setRoles(new ArrayList<Role>());

        // when
        userService.joinUser(user1);
        userService.deleteUser(user1.getUsername());

        // then
        assertFalse(userService.findById(user1.getId()).isPresent());
    }
}
