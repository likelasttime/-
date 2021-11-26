package likelasttime.Bulletin.Board.Controller;

import likelasttime.Bulletin.Board.Service.PostService;
import likelasttime.Bulletin.Board.domain.posts.Post;
import likelasttime.Bulletin.Board.validator.PostValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


@Controller
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Autowired
    private PostValidator postValidator;

    @GetMapping("/post/new")
    public String createForm() {
        return "post/createPostForm";
    }

    @PostMapping("/post/new")
    public String create(PostForm form) {

        Post post = new Post();
        post.setTitle(form.getTitle());
        post.setContent(form.getContent());
        post.setView(0);
        postService.join(post);

        return "redirect:/";
    }

    // 전체 게시글 조회
    @GetMapping("/post")
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
    @GetMapping("/post/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        if(id == null){
            model.addAttribute("post", new Post());
        }else{
            Post post= postService.findOne(id).orElse(null);
            postService.updateView(id);     // 조회수 증가
            model.addAttribute("post", post);
        }
        return "post/detail";
    }

    // 수정
    @PutMapping("/post/detail/{id}")
    public String greetingSubmit(@PathVariable("id") Long id, @Valid Post post, BindingResult bindingResult){
        postValidator.validate(post, bindingResult);
        if(bindingResult.hasErrors()){
            return "post/detail";
        }
        int view=postService.findOne(id).get().getView();
        post.setView(view);   // 조회수 저장
        postService.join(post);
        return "redirect:/post";
    }

    // 삭제
    @GetMapping("/post/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return "redirect:/";
    }

}
