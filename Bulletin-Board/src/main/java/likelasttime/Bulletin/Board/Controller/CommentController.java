package likelasttime.Bulletin.Board.Controller;

import likelasttime.Bulletin.Board.Service.CommentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

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

    // 수정
    @PutMapping("/update/{id}")
    public String update_comment(@PathVariable(value="id") Long comment_id, @RequestBody Map<String, String> map){
        Long post_id=Long.parseLong(map.get("post_id"));        // 게시글 번호
        commentService.update(comment_id, map.get("comment"));
        return "redirect:/post/detail/" + post_id;
    }

    // 삭제
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, @RequestBody Map<String, String> map) {
        Long post_id=Long.parseLong(map.get("post_id"));        // 게시글 번호
        commentService.delete(id);
        return "redirect:/post/detail/" + post_id;
    }


}
