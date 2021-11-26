package likelasttime.Bulletin.Board.validator;

import likelasttime.Bulletin.Board.domain.posts.Post;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;

@Component
public class PostValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz){
        return Post.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors){
        Post p=(Post) obj;
        if(StringUtils.isEmpty(p.getContent())){
            errors.rejectValue("content", "key", "내용을 입력하세요.");
        }
    }
}
