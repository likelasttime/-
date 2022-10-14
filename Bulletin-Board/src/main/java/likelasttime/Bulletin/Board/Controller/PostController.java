package likelasttime.Bulletin.Board.Controller;

import likelasttime.Bulletin.Board.Service.PostServiceImpl;
import likelasttime.Bulletin.Board.domain.posts.*;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping(path="/post")
public class PostController {
    private final PostServiceImpl postService;

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("post", new PostRequestDto());
        return "post/createPostForm";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("post") @Valid PostRequestDto post, BindingResult bindingResult,
                         @AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (bindingResult.hasErrors()) {
            return "post/createPostForm";
        }
        String username = principalDetails.getUsername();
        post.setAuthor(username);
        postService.create(post);
        return "redirect:/post";
    }

    // 게시글 조회(검색)
    @GetMapping("/search")
    public String list(Model model,
                       @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "") String keyword) {

        Page<PostResponseDto> postDto = postService.search(keyword, keyword, keyword, pageable);

        int pageSize = pageable.getPageSize();
        int start = (int) pageable.getOffset();
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
        List<PostResponseDto> postDto = new ArrayList<>();
        postDto = postService.findAllByCache();       // 캐시에서 조회
        if (postDto.isEmpty()) {             // 캐시에 없으면 DB에서 조회
            postDto = postService.findAll();
        }

        int len_postDto = postDto.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), len_postDto);

        Page<PostResponseDto> page = new PageImpl<>(postDto.subList(start, end), pageable, len_postDto);

        model.addAttribute("post", page);
        model.addAttribute("start", start);
        model.addAttribute("end", end);

        return "post/postList";
    }

    // 상세 게시판 조회
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        if (id == null) {
            model.addAttribute("post", new PostResponseDto());
        } else {
            PostResponseDto dto = postService.findById(id);
            model.addAttribute("post", dto);
        }
        return "post/detail";
    }

    @GetMapping("/update")
    public String updateForm(@RequestParam(value="id") Long id, Model model){
        PostResponseDto post=postService.findById(id);
        model.addAttribute("post", post);
        return "post/updateForm";
    }

    // 수정
    @PutMapping("/update/{id}")
    public String greetingSubmit(@PathVariable("id") Long id,
                                 @ModelAttribute("post") @Valid PostRequestDto post,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
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

/*
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> fileDownload(@PathVariable("fileId") Long fileId) throws IOException{
        FileDto fileDto=fileService.getFile(fileId);
        Path path= Paths.get(fileDto.getFilePath());
        Resource resource=new InputStreamResource(Files.newInputStream(path));
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octest-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileDto.getOriginFileName() + "\"")
                .body(resource);
    }
g
    @ResponseBody
    @GetMapping("/images/{id}")
    public Resource showImage(@PathVariable Long id) throws MalformedURLException{
        FileDto fileDto=fileService.getFile(id);
        return new UrlResource("file:" + fileDto.getFilePath());
    }

 */
}
