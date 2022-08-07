package likelasttime.Bulletin.Board.domain.posts;


import likelasttime.Bulletin.Board.Repository.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String author;

    private int view;

    private int comment_cnt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    @OrderBy("id desc")
    private List<Comment> comment;

    @Builder
    public Post(String title, String content, String author, int view, int comment_cnt) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.view = view;
        this.comment_cnt = comment_cnt;
    }

    public void update(String title, String content, int view, int comment_cnt) {
        this.title = title;
        this.content = content;
        this.view = view;
        this.comment_cnt = comment_cnt;
    }
}
