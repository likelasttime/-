package likelasttime.Bulletin.Board;

import likelasttime.Bulletin.Board.Config.SpringConfig;
import likelasttime.Bulletin.Board.Service.PostService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class PostApp {
    public static void main(String[] args){
        ApplicationContext applicationContext=new AnnotationConfigApplicationContext(SpringConfig.class);
        PostService postService=applicationContext.getBean("postService", PostService.class);
    }
}
