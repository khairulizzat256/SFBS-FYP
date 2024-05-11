package com.fyp.sfbs_fyp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/Admin")
public class AdminController {
    
    // Admin dashboard
    @GetMapping("/dashboard")
    public String dashboard(){
       
    


        return "AdminDashboard";
    }
}
