package P3.Project;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/dashboard") //Router starting point
public class DashboardController {
    int test = 1;

    @GetMapping
    public int basiOut() {
        return test;
    }
    
}
