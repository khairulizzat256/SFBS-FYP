package com.fyp.sfbs_fyp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fyp.sfbs_fyp.Service.StaffService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import org.springframework.ui.Model;


@Controller
@RequestMapping("/Admin")
public class AdminController {
    
    // Admin dashboard
    @GetMapping("/dashboard")
        

    public String dashboard(@RequestParam("uid") String uid, Model model) throws FirebaseAuthException{
        // Check if user is logged in using Firebase Auth
        UserRecord user = null;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        try {
            user = auth.getUser(uid);
            System.out.println("\n\nUser  " + user.getEmail() + " with UID: " + user.getUid() + " is logged in.\n\n" + user.getTokensValidAfterTimestamp() + "\n\n");
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return "redirect:/login?error=not_logged_in";
        }

        StaffService staffService = new StaffService();
        model.addAttribute("staffList", staffService.getStaffList());
        model.addAttribute("user", user);
        
        return "AdminDashboard";
    }
}
