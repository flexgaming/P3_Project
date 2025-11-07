package P3.Backend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    
    @GetMapping("/")
    public String redirectToFrontend() {
        // Redirect to Vite dev server on port 5173
        return "redirect:http://localhost:5173";
    }
}
