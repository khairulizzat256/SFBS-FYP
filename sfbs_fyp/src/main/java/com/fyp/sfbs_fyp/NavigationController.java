package com.fyp.sfbs_fyp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String showBookingPage(@RequestParam(value = "message", required = false) String message, Model model) {
        if (message != null) {
            model.addAttribute("message", message);
        }
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

    @GetMapping("/contact")
    public String showcontactPage() {
        return "contact"; 
    }

    @GetMapping("/AddStaff")
    public String showAddStaffPage() {
        return "addstaff"; 
    }

    @GetMapping("/signup")
    public String showsignup() {
        return "signup"; 
    }

    
}
