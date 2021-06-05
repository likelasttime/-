package likelasttime.Bulletin.Board.Controller;

import likelasttime.Bulletin.Board.Service.PostService;
import likelasttime.Bulletin.Board.domain.posts.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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
}
