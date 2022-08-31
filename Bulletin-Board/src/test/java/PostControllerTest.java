import likelasttime.Bulletin.Board.BulletinBoardApplication;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes= BulletinBoardApplication.class)
@AutoConfigureMockMvc
@WithMockUser
class PostControllerTest {
    @Autowired
    private MockMvc mvc;

    /*@Mock
    private PostServiceImpl postService;

    @Autowired
    private ObjectMapper objectMapper;

     */

    @Test
    public void createForm() throws Exception {
        String result = "post/createPostForm";
        this.mvc.perform(get("/post/new"))
                .andExpect(status().isOk())
                .andExpect(view().name(result))
                .andDo(print());
    }

    /*@Test
    public void detail() throws Exception {
        String title = "Test title";
        String content = "Test content";
        String author = "gorany";
        String result = "post/detail";
        String body = objectMapper.writeValueAsString(
                Post.builder().author(author).content(content).title(title).build()
        );

        mvc.perform(get("/post/detail/{id}", 1)
                        .content(body) //HTTP Body에 데이터를 담는다
                        .contentType(MediaType.APPLICATION_JSON) //보내는 데이터의 타입을 명시
                )
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }*/
}
