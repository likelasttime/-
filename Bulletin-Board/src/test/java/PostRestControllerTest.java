import com.fasterxml.jackson.databind.ObjectMapper;
import likelasttime.Bulletin.Board.Controller.PostRestController;
import likelasttime.Bulletin.Board.Service.PostService;
import likelasttime.Bulletin.Board.domain.posts.PostRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = PostRestController.class)
@WithMockUser
public class PostRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @BeforeEach
    public void before(){
        this.mockMvc= MockMvcBuilders.standaloneSetup(new PostRestController(this.postService))
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

    /*@Test
    public void createForm() throws Exception{
        mockMvc.perform(get("/post")
                        .contentType("application/json"))
                        .andDo(print())
                        .andExpect(status().isOk());
    }

     */

    @Test
    public void create_success() throws Exception{
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle("첫 번째 글");
        postRequestDto.setContent("내용");
        mockMvc.perform(post("/rest/posts")
                        .contentType("application/json")
                        .with(csrf())
                .content(objectMapper.writeValueAsString(postRequestDto)))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("/post"));
    }

    @Test
    public void create_fail_Title() throws Exception{
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setContent("content 작성");

        MvcResult result=mockMvc.perform(post("/rest/posts")
                        .contentType("application/json")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(postRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("post/createPostForm"))
                .andReturn();
    }

    @Test
    public void create_fail_Content() throws Exception{
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle("title 작성");

        MvcResult result=mockMvc.perform(post("/rest/posts")
                        .contentType("application/json")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(postRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("post/createPostForm"))
                .andReturn();
    }

    @Test
    public void detail() throws Exception{
        PostRequestDto postRequestDto=new PostRequestDto();

        mockMvc.perform(get("/rest/posts/{id}", 1L)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void list() throws Exception{
        String keyword="vlog";
        mockMvc.perform(get("/rest/posts/search")
                        .contentType("application/x-www-form-urlencoded")
                        .param("keyword", keyword))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void findAllPosts() throws Exception{
        mockMvc.perform(get("/rest/posts")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$['pageable']['paged']]").value("true"));
    }

   /* @Test
    public void updateForm() throws Exception{
        mockMvc.perform(get("/rest/post/{id}", 1L)
                        .param("id", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    */

    @Test
    public void greetingSubmit_validationFail_redirect() throws Exception{         // 유효성 검사 실패 후 redirect
        PostRequestDto postRequestDto=new PostRequestDto();
        postRequestDto.setAuthor("iu");

        mockMvc.perform(put("/rest/posts/{id}", 1)
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(postRequestDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("/post/detail"));
    }

    @Test
    public void greetingSubmit() throws Exception{
        PostRequestDto postRequestDto=new PostRequestDto();
        postRequestDto.setTitle("첫 게시글");
        postRequestDto.setAuthor("iu");
        postRequestDto.setContent("내용");

        mockMvc.perform(put("/rest/posts/{id}", 1)
                        .with(csrf())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(postRequestDto)))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(content().string("/post"));
    }

    @Test
    public void delete() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/rest/posts/{id}", 1)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}

