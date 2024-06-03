package com.fyp.sfbs_fyp;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fyp.sfbs_fyp.Service.BookingService;

@Controller
public class NavigationController {

    @Autowired
    BookingService bookingService;

    @GetMapping("/")
    public String test() {
        return "test"; 
    }

    @GetMapping("/home")
    public String home() {
        return "home"; 
    }

    @GetMapping("/booking")
    public String showBookingPage(@RequestParam(value = "message", required = false) String message, Model model) throws InterruptedException, ExecutionException {
        if (message != null) {
            model.addAttribute("message", message);
        }

        bookingService.retrieveBookingList();

        bookingService.getBookingEvents();
        model.addAttribute("response", bookingService.getBookingEvents());

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
