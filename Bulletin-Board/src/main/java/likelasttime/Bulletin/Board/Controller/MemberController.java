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
public class MemberController {
    private PostService postService;

    @Autowired
    public MemberController(PostService postService){
        this.postService=postService;
    }

    @GetMapping("/posts/new")
    public String createForm(){
        return "posts/createPostsForm";
    }

    @PostMapping("/posts/new")
    public String create(PostForm form){
        Post post =new Post();
        post.setTitle(form.getTitle());

        postService.join(post);

        return "redirect:/";
    }

    @GetMapping("/posts")
    public String list(Model model){
        List<Post> posts=postService.findPosts();
        model.addAttribute("posts", posts);
        return "posts/postsList";
    }
}
