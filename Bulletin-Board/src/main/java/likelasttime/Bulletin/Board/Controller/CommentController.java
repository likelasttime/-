package likelasttime.Bulletin.Board.Controller;

import likelasttime.Bulletin.Board.Service.CommentService;
import likelasttime.Bulletin.Board.domain.posts.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(path="/comment")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/comments/{id}")
    public String findAll(@PathVariable String id, Model model){
        model.addAttribute("commentList", commentService.getCommentList(Long.valueOf(id)));
        return "/post/detail :: #commentTable";
    }

    @ResponseBody
    @PostMapping("/valid")
    public Result isValid(@RequestBody Map<String, String> map){
        Result result=new Result();
        String comment=map.get("comment");
        commentService.validate(comment, result);
        return result;
    }

    @PostMapping("/view")
    public String viewPostMethod(Model model, @RequestBody  Map<String, String> map,
                                 Principal principal) {
        String comment=map.get("comment");
        Long post_id=Long.valueOf(map.get("postId"));

        commentService.commentSave(principal.getName(), post_id, comment);

        // 댓글 리스트 추가
        model.addAttribute("commentList", commentService.getCommentList(post_id));

        return "/post/detail :: #commentTable";
    }

    // 수정
    @PutMapping("/update")
    public String update_comment(@RequestBody Map<String, String> map, Model model){
        String comment=map.get("comment");
        Long comment_id=Long.valueOf(map.get("comment_id"));
        Long post_id=Long.parseLong(map.get("post_id"));
        commentService.update(comment_id, comment);
        model.addAttribute("commentList", commentService.getCommentList(post_id));
        return "/post/detail :: #commentTable";
    }

    // 삭제
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, @RequestBody Map<String, String> map, Model model) {
        Long post_id=Long.parseLong(map.get("post_id"));        // 게시글 번호
        commentService.delete(id, post_id);
        model.addAttribute("commentList", commentService.getCommentList(post_id));
        return "/post/detail :: #commentTable";
    }


}
