package likelasttime.Bulletin.Board.domain.posts;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable=false, length=20)
    private String username;
    private String name;
    private String password;
    private Boolean enabled;
    private String email;
    private String phone;

    @ManyToMany
    @JoinTable(
            name="user_role",
            joinColumns=@JoinColumn(name="user_id"),
            inverseJoinColumns=@JoinColumn(name="role_id")
    )

    private List<Role> roles=new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) { this.email = email; }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void update(User newUser) {
        this.name=newUser.name;
        this.password=newUser.password;
        this.email=newUser.email;
        this.phone=newUser.phone;
    }
}
