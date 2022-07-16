package likelasttime.Bulletin.Board.Controller;

import likelasttime.Bulletin.Board.Service.PostServiceImpl;
import likelasttime.Bulletin.Board.domain.posts.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.cache.annotation.CacheEvict;
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
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping(path="/post")
public class PostController {
    private final PostServiceImpl postService;
    private final ModelMapper modelMapper;

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("post", new PostRequestDto());
        return "post/createPostForm";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("post") @Valid PostRequestDto post, BindingResult bindingResult) {
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
        Page<PostRequestDto> postDto=post.map(p -> modelMapper.map(p, PostRequestDto.class));

        int start = Math.max(1, postDto.getPageable().getPageNumber() - 4);
        int end = Math.min(postDto.getTotalPages(), postDto.getPageable().getPageNumber() + 4);

        model.addAttribute("post", postDto);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        return "post/postList";
    }

    // 상세 게시판 조회
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        if(id == null){
            model.addAttribute("post", new PostRequestDto());
        }else{
            Post post= postService.findById(id).orElse(null);
            postService.updateView(id);     // 조회수 증가
            PostResponseDto postResponseDto=PostResponseDto.builder()
                    .post(post)
                    .build();
            model.addAttribute("post", postResponseDto);
            List<CommentResponseDto> comments=postResponseDto.getComment();
            if(comments != null && !comments.isEmpty()){
                model.addAttribute("commentList", comments);
            }
        }
        return "post/detail";
    }

    // 수정
    @PutMapping("/detail/{id}")
    public String greetingSubmit(@PathVariable("id") Long id,
                                 @ModelAttribute("post") @Valid PostRequestDto post,
                                 BindingResult bindingResult){
        post.setAuthor(((postService.findById(id).get()).getAuthor()));     // 작성자
        if(bindingResult.hasErrors()){
            return "/post/detail";
        }
        postService.update(id, post);
        return "redirect:/post";
    }

    // 삭제
    @CacheEvict(value="deletePost")
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return "redirect:/post";
    }

}
