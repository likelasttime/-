package likelasttime.Bulletin.Board;

import likelasttime.Bulletin.Board.Config.SpringConfig;
import likelasttime.Bulletin.Board.Service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class UserApp {
    public static void main(String[] args){
        ApplicationContext applicationContext=new AnnotationConfigApplicationContext(SpringConfig.class);
        UserService userService=applicationContext.getBean("userService", UserService.class);
    }
}
