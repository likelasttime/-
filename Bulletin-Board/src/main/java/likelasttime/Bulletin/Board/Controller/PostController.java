package likelasttime.Bulletin.Board.Controller;

import likelasttime.Bulletin.Board.Service.PostServiceImpl;
import likelasttime.Bulletin.Board.domain.posts.Post;
import likelasttime.Bulletin.Board.validator.PostValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@Controller
@RequiredArgsConstructor
@RequestMapping(path="/post")
public class PostController {
    private final PostServiceImpl postService;
    private final PostValidator postValidator;

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("post", new Post());
        return "post/createPostForm";
    }

    @PostMapping("/new")
    public String create(@Valid Post post, BindingResult bindingResult) {
        postValidator.validate(post, bindingResult);
        if(bindingResult.hasErrors()){
            return "post/createPostForm";
        }
        Object principal= SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        post.setAuthor(((UserDetails)principal).getUsername());     // 작성자=로그인한 유저 id
        postService.create(post);
        return "redirect:/post";
    }

    // 전체 게시글 조회
    @GetMapping
    public String list(Model model,
                       @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "") String keyword
                       ) {

        Page<Post> post = postService.search(keyword, keyword, keyword, pageable);
        int start = Math.max(1, post.getPageable().getPageNumber() - 4);
        int end = Math.min(post.getTotalPages(), post.getPageable().getPageNumber() + 4);

        model.addAttribute("post", post);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        return "/post/postList";
    }

    // 상세 게시판 조회
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        if(id == null){
            model.addAttribute("post", new Post());
        }else{
            Post post= postService.findById(id).orElse(null);
            postService.updateView(id);     // 조회수 증가
            model.addAttribute("post", post);
        }
        return "post/detail";
    }

    // 수정
    @PutMapping("/detail/{id}")
    public String greetingSubmit(@PathVariable("id") Long id, @Valid Post post, BindingResult bindingResult){
        postValidator.validate(post, bindingResult);
        if(bindingResult.hasErrors()){
            return "post/detail";
        }
        int view=postService.findById(id).get().getView();
        post.setView(view);   // 조회수 저장
        String author=postService.findById(id).get().getAuthor();
        post.setAuthor(author);
        postService.create(post);
        return "redirect:/post";
    }

    // 삭제
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return "redirect:/post";
    }

}
