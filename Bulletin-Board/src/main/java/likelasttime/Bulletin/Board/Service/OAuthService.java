package likelasttime.Bulletin.Board.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import likelasttime.Bulletin.Board.domain.posts.Token;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class OAuthService {
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
}
