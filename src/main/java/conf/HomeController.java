package conf;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;


@Controller
public class HomeController {
    @RequestMapping("/")
    @ResponseBody
    public String home() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return "Welcome, " + username;
    }
}
