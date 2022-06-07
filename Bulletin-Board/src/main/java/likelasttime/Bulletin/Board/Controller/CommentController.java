package likelasttime.Bulletin.Board.Controller;

import likelasttime.Bulletin.Board.Service.CommentService;
import likelasttime.Bulletin.Board.Service.PostService;
import likelasttime.Bulletin.Board.domain.posts.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping(path="/comment")
public class CommentController {
    private final CommentService commentService;
    private final ModelMapper modelMapper;
    private final PostService postService;

    @PostMapping("/write")
    public String create(@RequestParam("post_id") Long post_id,
                         @ModelAttribute("commentRequestDto") @Valid CommentRequestDto commentRequestDto,
                         BindingResult bindingResult,
                         Principal principal,
                         Model model
                         ){
        if(bindingResult.hasErrors()){
            Post post=postService.findById(post_id).get();
            PostResponseDto postResponseDto=PostResponseDto.builder()
                    .post(post)
                    .build();
            model.addAttribute("post", postResponseDto);
            List<CommentResponseDto> comments=postResponseDto.getComment();
            if(comments != null && !comments.isEmpty()){
                model.addAttribute("commentList", comments);
            }
            return "post/detail";
        }
        commentService.commentSave(principal.getName(), post_id, commentRequestDto.getComment());
        return "redirect:/post/detail/" + post_id;
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
