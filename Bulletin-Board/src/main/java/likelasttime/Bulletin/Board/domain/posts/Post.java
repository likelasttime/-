package likelasttime.Bulletin.Board.domain.posts;

import javax.validation.constraints.NotNull;
import likelasttime.Bulletin.Board.Repository.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
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
}
