package likelasttime.Bulletin.Board.domain.posts;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class PrincipalDetails implements UserDetails, OAuth2User {
    private User user;
    private Map<String, Object> attributes;

    public PrincipalDetails(User user){
        this.user = user;
    }

    // OAuth2 로그인
    public PrincipalDetails(User user, Map<String, Object> attributes){
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public String getPassword(){
        return user.getPassword();
    }

    @Override
    public String getUsername(){
        return user.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        String role = "ROLE_GUEST";
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority(){
            @Override
            public String getAuthority(){
                return role;
            }
        });
        return collect;
    }

    @Override
    // 만료 여부
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    // 잠금 여부
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    // 만료 여부
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    // 회원 사용 여부
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public String getName() {
        return null;
    }
}
