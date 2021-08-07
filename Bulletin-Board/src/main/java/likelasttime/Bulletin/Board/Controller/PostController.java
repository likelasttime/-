package likelasttime.Bulletin.Board.Controller;

import likelasttime.Bulletin.Board.Service.PostService;
import likelasttime.Bulletin.Board.domain.posts.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/post/new")
    public String createForm() {
        return "post/createPostForm";
    }

    @PostMapping("/post/new")
    public String create(PostForm form) {
        Post post = new Post();
        post.setTitle(form.getTitle());
        post.setAuthor(form.getAuthor());
        post.setContent(form.getContent());
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
        Post post = postService.findOne(id);
        postService.updateView(id);     // 조회수 증가
        post.setView(post.getView()+1);     // 게시글 들어가면 바로 조회수 증가 시키기 위해서
        model.addAttribute("post", post);
        return "post/detail";
    }


    //수정(데베에서 값 불러오기)
    @GetMapping("/post/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        Post post = postService.findOne(id);
        model.addAttribute("post", post);
        return "post/updateForm";
    }

    // 수정한 내용 저장
    @PutMapping("/post/edit/{id}")
    public String update(Post post, @PathVariable("id") Long id) {
        int view=postService.findOne(id).getView();
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
