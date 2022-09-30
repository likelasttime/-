package likelasttime.Bulletin.Board.domain.posts;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Token {
    String access_token;

    String refresh_token;

    String token_type;

    Integer expires_in;

    String scope;

    Integer refresh_token_expires_in;
}
