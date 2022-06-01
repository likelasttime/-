package likelasttime.Bulletin.Board.domain.posts;

import likelasttime.Bulletin.Board.Repository.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String comment;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

}
