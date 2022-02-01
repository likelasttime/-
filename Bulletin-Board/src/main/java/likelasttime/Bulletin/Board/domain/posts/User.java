package likelasttime.Bulletin.Board.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable=false)
    private Boolean enabled;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @ManyToMany
    @JoinTable(
            name="user_role",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="role_id")
    )
    private List<Role> roles;

    @Builder
    public User(Long id, String name, String phone, String email, String password, String username, Boolean enabled, List<Role> roles){
        this.id=id;
        this.name=name;
        this.phone=phone;
        this.email=email;
        this.password=password;
        this.username=username;
        this.enabled=enabled;
        this.roles=roles;
    }

    public void update(User newUser) {
        this.name=newUser.name;
        this.password=newUser.password;
        this.email=newUser.email;
        this.phone=newUser.phone;
    }
}
