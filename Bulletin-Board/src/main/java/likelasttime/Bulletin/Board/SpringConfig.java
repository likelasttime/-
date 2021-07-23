package likelasttime.Bulletin.Board;

import likelasttime.Bulletin.Board.Repository.JpaPostRepository;
import likelasttime.Bulletin.Board.Repository.MemoryPostRepository;
import likelasttime.Bulletin.Board.Repository.PostRepository;
import likelasttime.Bulletin.Board.Repository.SpringDataJpaPostRepository;
import likelasttime.Bulletin.Board.Service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Configuration
public class SpringConfig {
    private final SpringDataJpaPostRepository postRepository;

    public SpringConfig(SpringDataJpaPostRepository postRepository){
        this.postRepository=postRepository;
    }

}
