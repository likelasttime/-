package likelasttime.Bulletin.Board.Controller;

import likelasttime.Bulletin.Board.Service.CommentService;
import likelasttime.Bulletin.Board.domain.posts.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(path="/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/write")
    @ResponseBody
    public Result create(@RequestBody Map<String, String> map,
                         Principal principal){
        Result result=new Result();
        String comment=map.get("comment");
        if (comment.isBlank()){
            result.setBlank(true);
            return result;
        }else if (comment.length() > 10000) {
            result.setMax(true);
            return result;
        }
        result.setBlank(false);
        result.setMax(false);
        Long post_id=Long.parseLong(map.get("post_id"));
        commentService.commentSave(principal.getName(), post_id, comment);
        return result;
    }

    // 수정
    @PutMapping("/update/{id}")
    @ResponseBody
    public Result update_comment(@PathVariable(value="id") Long comment_id,
                                 @RequestBody Map<String, String> map){
        Result result=new Result();
        String comment=map.get("comment");
        if (comment.isBlank()){
            result.setBlank(true);
            return result;
        }else if (comment.length() > 10000) {
            result.setMax(true);
            return result;
        }
        result.setBlank(false);
        result.setMax(false);
        commentService.update(comment_id, comment);
        return result;
    }

    // 삭제
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, @RequestBody Map<String, String> map) {
        Long post_id=Long.parseLong(map.get("post_id"));        // 게시글 번호
        commentService.delete(id);
        return "redirect:/post/detail/" + post_id;
    }


}
