package likelasttime.Bulletin.Board.Controller;

import likelasttime.Bulletin.Board.Service.PostService;
import likelasttime.Bulletin.Board.domain.posts.PostRequestDto;
import likelasttime.Bulletin.Board.domain.posts.PostResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PostRestController {
    @Autowired
    private final PostService postService;

    public PostRestController(PostService postService) {
        this.postService=postService;
    }

    /*@GetMapping("/post")
    public String createForm(){
        return "post/createPostForm";
    }*/

    @PostMapping("/post")
    public PostResponseDto create(@Valid @RequestBody PostRequestDto postRequestDto){
        PostResponseDto postResponseDto = postService.create(postRequestDto);
        return postResponseDto;
    }

    /*@GetMapping("/post/{keyword}")
    public Page<PostResponseDto> list(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                                      @PathVariable String keyword){
        return postService.search(keyword, keyword, keyword, pageable);
    }

     */

    @GetMapping("/post/{id}")
    public ResponseEntity<PostResponseDto> detail(@PathVariable("id") Long id) {
        PostResponseDto dto = postService.findById(id);
        return new ResponseEntity<PostResponseDto>(dto, HttpStatus.OK) ;
    }

    @GetMapping("/post")
    public ResponseEntity<Page<PostResponseDto>> findAllPosts(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        List<PostResponseDto> postDto = new ArrayList<>();
        postDto = postService.findAllByCache();       // 캐시에서 조회
        if (postDto.isEmpty()) {             // 캐시에 없으면 DB에서 조회
            postDto = postService.findAll();
        }

        int len_postDto = postDto.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), len_postDto);

        Page<PostResponseDto> page = new PageImpl<>(postDto.subList(start, end), pageable, len_postDto);
        return new ResponseEntity<Page<PostResponseDto>>(page, HttpStatus.OK);
    }

    /*@GetMapping("/post/{id}")
    public PostResponseDto updateForm(@PathVariable("id") Long id){
        PostResponseDto post=postService.findById(Long.valueOf(id));
        return post;
    }

     */

    @PutMapping("/post/{id}")
    public ResponseEntity<PostResponseDto> greetingSubmit(@PathVariable("id") Long id,
                                 @Valid PostRequestDto post,
                                 BindingResult bindingResult) {
        //post.setAuthor(((postService.findById(id)).getAuthor()));     // 작성자 -> postService.update로 옮기기
        /*if (bindingResult.hasErrors()) {
            return "/post/detail";
        }*/

        PostResponseDto postResponseDto = postService.update(id, post);
        return new ResponseEntity<PostResponseDto>(postResponseDto, HttpStatus.OK);
    }

    @DeleteMapping("/post/{id}")
    public String delete(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return "redirect:/post";
    }

    /*@GetMapping("/post")
    public Page<PostResponseDto> findAllPosts(){
        List<PostResponseDto> postDto = new ArrayList<>();
        postDto = postService.findAllByCache();       // 캐시에서 조회
        if (postDto.isEmpty()) {             // 캐시에 없으면 DB에서 조회
            postDto = postService.findAll();
        }
        Page<PostResponseDto> page = new PageImpl<>(postDto.subList(1, 5), pageable, len_postDto);
        return;
    }

     */
}
