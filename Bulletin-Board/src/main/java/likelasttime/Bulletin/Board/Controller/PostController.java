package likelasttime.Bulletin.Board.Controller;

import likelasttime.Bulletin.Board.Service.PostServiceImpl;
import likelasttime.Bulletin.Board.domain.posts.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import java.io.IOException;
import java.util.ArrayList;
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

    // 게시글 조회(검색)
    @GetMapping("/search")
    public String list(Model model,
                       @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "") String keyword) {

        Page<PostResponseDto> postDto = postService.search(keyword, keyword, keyword, pageable);

        int pageSize=pageable.getPageSize();
        int start=(int) pageable.getOffset();
        int end = Math.min((start + pageSize), pageSize);

        model.addAttribute("post", postDto);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        return "post/postList";
    }

    // 전체 게시글 조회(findAll)
    @GetMapping
    public String findAllPosts(Model model,
                       @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        List<PostResponseDto> postDto=new ArrayList<>();
        postDto=postService.findAllByCache();       // 캐시에서 조회
        if(postDto.isEmpty()) {             // 캐시에 없으면 DB에서 조회
            postDto = postService.findAll();
        }

        int len_postDto=postDto.size();
        int start=(int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), len_postDto);

        Page<PostResponseDto> page=new PageImpl<>(postDto.subList(start, end), pageable, len_postDto);

        model.addAttribute("post", page);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        return "post/postList";
    }

    // 상세 게시판 조회
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) throws IOException {
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
                                 BindingResult bindingResult) throws IOException{
        post.setAuthor(((postService.findById(id).get()).getAuthor()));     // 작성자
        if(bindingResult.hasErrors()){
            return "/post/detail";
        }
        postService.update(id, post);
        return "redirect:/post";
    }

    // 삭제
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return "redirect:/post";
    }

}
