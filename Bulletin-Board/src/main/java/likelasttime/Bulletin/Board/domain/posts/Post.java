package likelasttime.Bulletin.Board.domain.posts;

import javax.validation.constraints.NotNull;
import likelasttime.Bulletin.Board.Repository.BaseTimeEntity;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getView() { return view; }

    public void setView(int view) { this.view = view; }

}
