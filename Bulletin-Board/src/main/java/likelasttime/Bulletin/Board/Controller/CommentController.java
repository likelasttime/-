package likelasttime.Bulletin.Board.Controller;

import likelasttime.Bulletin.Board.Service.CommentService;
import likelasttime.Bulletin.Board.Service.PostService;
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
    private final PostService postService;

    /*@PostMapping("/write")
    @ResponseBody
    public Result create(@RequestBody Map<String, String> map,
                         Principal principal, Model model){
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

     */

    @PostMapping("/view")
    public String viewPostMethod(Model model, @RequestBody Map<String, String> map, Principal principal) {
        // DB 댓글 추가
        String comment=map.get("comment");
        Long post_id=Long.parseLong(map.get("post_id"));
        commentService.commentSave(principal.getName(), post_id, comment);

        // 댓글 리스트 추가
        model.addAttribute("commentList", postService.getCommentList(post_id));

        return "/post/detail :: #commentTable";
    }

    // 수정
    @PutMapping("/update/{id}")
    public String update_comment(@PathVariable(value="id") Long comment_id,
                                 @RequestBody Map<String, String> map, Model model){
        String comment=map.get("comment");
        Long post_id=Long.parseLong(map.get("post_id"));
        commentService.update(comment_id, comment);
        model.addAttribute("commentList", postService.getCommentList(post_id));
        return "/post/detail :: #commentTable";
    }

    // 삭제
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, @RequestBody Map<String, String> map, Model model) {
        Long post_id=Long.parseLong(map.get("post_id"));        // 게시글 번호
        commentService.delete(id, post_id);
        model.addAttribute("commentList", postService.getCommentList(post_id));
        return "/post/detail :: #commentTable";
    }


}
