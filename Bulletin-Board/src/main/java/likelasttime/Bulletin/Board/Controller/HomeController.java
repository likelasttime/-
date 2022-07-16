package likelasttime.Bulletin.Board.Controller;

import likelasttime.Bulletin.Board.Service.PostServiceImpl;
import likelasttime.Bulletin.Board.domain.posts.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final PostServiceImpl postService;

    @GetMapping("/")
    public String home(Model model){
        List<PostResponseDto> post=postService.findByRank();       // 조회수를 기준으로 상위 10개 게시글
        model.addAttribute("rank_post", post);
        return "home";
    }
}
