import com.fasterxml.jackson.databind.ObjectMapper;
import likelasttime.Bulletin.Board.Controller.PostRestController;
import likelasttime.Bulletin.Board.Service.PostService;
import likelasttime.Bulletin.Board.domain.posts.PostRequestDto;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    /*@Test
    public void createForm() throws Exception{
        mockMvc.perform(get("/post")
                        .contentType("application/json"))
                        .andDo(print())
                        .andExpect(status().isOk());
    }

     */

    @Test
    public void create() throws Exception{
        PostRequestDto postRequestDto = new PostRequestDto();
        postRequestDto.setTitle("첫 번째 글");
        mockMvc.perform(post("/post")
                        .contentType("application/json")
                        .with(csrf())
                        .content(objectMapper.writeValueAsString(postRequestDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void detail() throws Exception{
        PostRequestDto postRequestDto=new PostRequestDto();

        mockMvc.perform(get("/post/{id}", 1L)
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /*@Test
    public void list() throws Exception{
        mockMvc.perform(get("/post/{keyword}", "안녕")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk());
    }

     */

    @Test
    public void findAllPosts() throws Exception{
        mockMvc.perform(get("/post")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$['pageable']['paged']]").value("true"));
    }

    /*@Test
    public void updateForm() throws Exception{
        mockMvc.perform(get("/post/{id}", 1L)
                        .param("id", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

     */

    @Test
    public void greetingSubmit() throws Exception{
        PostRequestDto postRequestDto=new PostRequestDto();
        postRequestDto.setAuthor("happy");
        mockMvc.perform(put("/post/{id}", 1)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void delete() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/post/{id}", 1)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}

