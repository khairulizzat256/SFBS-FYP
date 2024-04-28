package com.fyp.sfbs_fyp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {

    @GetMapping("/")
    public String test() {
        return "test"; 
    }

    @GetMapping("/home")
    public String home() {
        return "home"; 
    }

    @GetMapping("/booking")
    public String showBookingPage() {
        return "booking"; 
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; 
    }

    @GetMapping("/aboutUs")
    public String showaboutUsPage() {
        return "aboutUs"; 
    }

    
}
