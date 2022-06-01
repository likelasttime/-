package likelasttime.Bulletin.Board.Controller;

import likelasttime.Bulletin.Board.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping(path="/comment")
public class CommentController {
    private final CommentService commentService;
    private final ModelMapper modelMapper;

    @PostMapping("/write")
    public String create(@RequestParam("post_id") Long post_id, @RequestParam("reply") String content, Principal principal){
        commentService.commentSave(principal.getName(), post_id, content);
        return "redirect:/post/detail/" + post_id;
    }

}
