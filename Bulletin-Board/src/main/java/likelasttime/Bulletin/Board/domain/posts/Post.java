package likelasttime.Bulletin.Board.domain.posts;

import likelasttime.Bulletin.Board.Repository.BaseTimeEntity;
import javax.persistence.*;

@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable=false)
    private String title;

    @Column(columnDefinition="TEXT")
    private String content;

    private String author;

    private int view=0;

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
