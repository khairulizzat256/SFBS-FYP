package com.fyp.sfbs_fyp.Controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class AuthenticationController {

    @PostMapping("/Admin")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password) {
        // Verify the credentials here
        if (email.equals("izzat.hasbabu@gmail.com") && password.equals("admin123")) {
            // Credentials are valid
            return "AdminDashboard";
        }
        else
        {
            // Credentials are invalid
            return "login";
        }
    }
}
