package likelasttime.Bulletin.Board.domain.posts;

import javax.validation.constraints.NotNull;
import likelasttime.Bulletin.Board.Repository.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable=false)
    @Size(min=2, max=30, message="제목은 2자이상 30자 이하입니다.")
    private String title;

    @Column(columnDefinition="TEXT")
    private String content;

    private String author;

    private int view;

    @OneToMany(mappedBy="post", fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
    private List<Comment> comment;

    @Builder
    public Post(String title, String content, String author, int view){
        this.title=title;
        this.content=content;
        this.author=author;
        this.view=view;
    }

    public void update(String title, String content, int view){
        this.title=title;
        this.content=content;
        this.view=view;
    }
}
