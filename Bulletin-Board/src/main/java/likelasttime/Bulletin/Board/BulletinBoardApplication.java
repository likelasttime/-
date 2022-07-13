package likelasttime.Bulletin.Board;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EnableCaching
public class BulletinBoardApplication {
	public static void main(String[] args) {
		SpringApplication.run(BulletinBoardApplication.class, args);
	}
}
