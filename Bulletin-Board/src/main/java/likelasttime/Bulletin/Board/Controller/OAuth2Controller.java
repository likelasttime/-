package likelasttime.Bulletin.Board.Controller;

import likelasttime.Bulletin.Board.Service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@Controller
@RequiredArgsConstructor
@RequestMapping(path="/oauth2")
public class OAuth2Controller {
    private final OAuthService oAuthService;
    private final AuthenticationManager authenticationManager;

    @GetMapping("/kakao")
    public String getKakaoAuthorization(@RequestParam String code) throws IOException {
        System.out.println(code);
        String access_token = oAuthService.getKakaoAccessToken(code);
        HashMap<String, Object> map = oAuthService.getKakaoUserInfo(access_token);

        // 로그인 처리
        Authentication kakaoUsernamePassword = new UsernamePasswordAuthenticationToken(map.get("email"), map.get("password"));
        Authentication authentication = authenticationManager.authenticate(kakaoUsernamePassword);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/";
    }
}
