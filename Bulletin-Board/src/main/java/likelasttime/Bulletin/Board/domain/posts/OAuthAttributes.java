package likelasttime.Bulletin.Board.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Map;

@Getter
@Setter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String id;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String mobile;
    private String password;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String id, String nameAttributeKey, String name, String email, String mobile, String password){
        this.attributes = attributes;
        this.id = id;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
        if("naver".equals(registrationId)){
            return ofNaver("id", attributes);
        }else if("kakao".equals(registrationId)){
            return ofKakao("id", attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes){
        Map<String, Object> response = (Map<String, Object>)attributes.get("kakao_account");
        String pwd = "abc" + (String)response.get("id") + "!";
        return OAuthAttributes.builder()
                .id((String)attributes.get("id"))
                .name((String)response.get("name"))
                .email((String)response.get("email"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .password(pwd)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes){
        Map<String, Object> response = (Map<String, Object>)attributes.get("response");
        String pwd = "abc" + (String)response.get("id") + "!";
        return OAuthAttributes.builder()
                .id((String)response.get("id"))
                .name((String)response.get("name"))
                .email((String)response.get("email"))
                .mobile(((String)response.get("mobile")).replaceAll("-", ""))       // - 제거
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .password(pwd)
                .build();
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes){
        String pwd = "abc" + (String)attributes.get("sub") + "!";
        String mobile = (String)attributes.getOrDefault("phone", null);
        if(mobile != null){
            mobile.replaceAll("-", "");
        }
        return OAuthAttributes.builder()
                .id((String)attributes.get("sub"))
                .name((String)attributes.get("name"))
                .email((String)attributes.get("email"))
                .mobile(mobile)       // - 제거
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .password(pwd)
                .build();
    }

    public User toEntity(){
        Role role = new Role();
        role.setId(2L);
        role.setName("GUEST");
        ArrayList<Role> arrayList = new ArrayList<>();
        arrayList.add(role);

        return User.builder()
                .name(name)
                .username(email)
                .password(password)
                .email(email)
                .phone(mobile)
                .roles(arrayList)
                .enabled(true)
                .build();
    }
}
