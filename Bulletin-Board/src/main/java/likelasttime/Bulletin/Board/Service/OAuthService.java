package likelasttime.Bulletin.Board.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import likelasttime.Bulletin.Board.Repository.UserRepository;
import likelasttime.Bulletin.Board.domain.posts.Role;
import likelasttime.Bulletin.Board.domain.posts.Token;
import likelasttime.Bulletin.Board.domain.posts.KakaoUserInfo;
import likelasttime.Bulletin.Board.domain.posts.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class OAuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String getKakaoAccessToken(String code) throws IOException {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=db5b74e6c640443403b9705680d9dbac");
            sb.append("&redirect_uri=http://localhost:8080/oauth2/kakao");
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("response body : " + result);

            ObjectMapper objectMapper = new ObjectMapper();

            Token token = objectMapper.readValue(result, Token.class);
            access_Token = token.getAccess_token();
            refresh_Token = token.getRefresh_token();

            System.out.println("access_token : " + access_Token);
            System.out.println("refresh_token : " + refresh_Token);

            br.close();
            bw.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return access_Token;
    }

    public HashMap<String, Object> getKakaoUserInfo(String token) throws MalformedURLException, IOException{
        HashMap<String, Object> map = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try{
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token);

            // 200 코드면 성공
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode : " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while((line = br.readLine()) != null){
                result += line;
            }
            System.out.println("response body : " + result);

            ObjectMapper objectMapper = new ObjectMapper();
            KakaoUserInfo kakaoUserInfo = objectMapper.readValue(result, KakaoUserInfo.class);
            String nickName = kakaoUserInfo.getKakao_account().getProfile().getNickname();
            String email = kakaoUserInfo.getKakao_account().getEmail();
            String id = kakaoUserInfo.getId().toString();
            if(email.isEmpty()) {
                email = "a" + id + "@temp.com";
            }
            String tempPassword = "abc" + id + "!";
            String encodedPassword = passwordEncoder.encode(tempPassword);
            String tempPhone = "01012341234";
            Role role = new Role();
            role.setId(2L);
            role.setName("ROLE_GUEST");
            ArrayList roles = new ArrayList();
            roles.add(role);

            if(!userRepository.existsByEmail(email)){
                User user = User.builder()
                        .name(nickName)
                        .username(email)
                        .email(email)
                        .password(encodedPassword)
                        .phone(tempPhone)
                        .roles(new ArrayList<>())
                        .enabled(true)
                        .build();

                user.getRoles().add(role);
                userRepository.save(user);
            }

            map.put("nickname", nickName);
            map.put("email", email);
            map.put("password", tempPassword);

            br.close();

        }catch(IOException e){
            e.printStackTrace();
        }

        return map;
    }
}
