package likelasttime.Bulletin.Board.Controller;

import likelasttime.Bulletin.Board.Service.PostService;
import likelasttime.Bulletin.Board.domain.posts.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService){
        this.postService=postService;
    }

    @GetMapping("/post/new")
    public String createForm(){
        return "post/createPostForm";
    }

    @PostMapping("/post/new")
    public String create(PostForm form){
        Post post =new Post();
        post.setTitle(form.getTitle());
        post.setAuthor(form.getAuthor());
        post.setContent(form.getContent());

        postService.join(post);

        return "redirect:/";
    }

    @GetMapping("/post")
    public String list(Model model){
        List<Post> post=postService.findPost();
        model.addAttribute("post", post);
        return "post/postList";
    }
    // 상세 게시판 조회
    @GetMapping("/post/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        //postService.findOne(id).ifPresent(o->model.addAttribute("post", o));
        Post post=postService.findOne(id);
        model.addAttribute("post", post);
        return "post/detail";
    }

    //수정
    @GetMapping("/post/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        //postService.findOne(id).ifPresent(o->model.addAttribute("post", o));
        Post post=postService.findOne(id);
        model.addAttribute("post", post);
        return "post/updateForm";
    }

    @PostMapping("/post/edit/{id}")
    public String update(Post post){
        postService.join(post);
        return "redirect:/";
    }

    //삭제
    @GetMapping("/post/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        postService.deletePost(id);
        return "redirect:/";
    }


}
