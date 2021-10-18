package likelasttime.Bulletin.Board.Controller;

import likelasttime.Bulletin.Board.Repository.UserRepository;
import likelasttime.Bulletin.Board.domain.posts.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    public UserRepository userRepository;

    @GetMapping("/join/new")
    public String joinForm(){
        return "user/joinForm";
    }

    @PostMapping("/join/new")
    public String join(User user){
        userRepository.save(user);
        return "redirect:/user/list";
    }

    @GetMapping("/user/list")
    public String list(Model model){
        model.addAttribute("users", userRepository.findAll());
        return "/user/list";
    }

    @GetMapping("/user/{id}/form")
    public String updateForm(@PathVariable Long id, Model model){
        model.addAttribute("user", userRepository.findById(id).get());
        return "/user/updateForm";
    }

    @PutMapping("/user/{id}")
    public String update(@PathVariable Long id, User newUser){
        User user=userRepository.findById(id).get();
        user.update(newUser);
        userRepository.save(user);
        return "redirect:/user/list";
    }

    @GetMapping("/user/loginForm")
    public String loginForm(){
        return "/user/login";
    }

    @PostMapping("/user/login")
    public String login(String userId, String password, HttpSession session){
        Optional<User> user=userRepository.findByUserId(userId);
        if(user.isEmpty()){
            System.out.println("Login Failure!");
            return "redirect:/user/loginForm";
        }
        if(!user.get().getPassword().equals(password)){
            System.out.println("Login Failure!");
            return "redirect:/user/loginForm";
        }

        System.out.println("Login Success!");
        session.setAttribute("user", user);

        return "redirect:/post";
    }
}
