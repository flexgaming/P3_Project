package P3.Backend;

import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.view.RedirectView;

//Test for a seperate URL path
@RestController
@RequestMapping("/dashboard") //Router starting point
public class DashboardController {
    int test = 1;

    @GetMapping
    public int basicOut() {
        return test; //Return int as test
    }

    @GetMapping(value = "/redirect") //Reroute to seperate URL path
    public RedirectView rerouteToDockers() {
        RedirectView redirect = new RedirectView();
        redirect.setUrl("/dockers");
        return redirect;
    }
        
    
}
