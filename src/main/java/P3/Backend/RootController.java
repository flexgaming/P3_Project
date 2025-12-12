package P3.Backend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    
    @GetMapping("/")
    public String redirectToFrontend() {
        return "redirect:http://localhost:5173";
    }
}
