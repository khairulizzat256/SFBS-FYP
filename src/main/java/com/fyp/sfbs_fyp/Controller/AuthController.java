package com.fyp.sfbs_fyp.Controller;

import com.fyp.sfbs_fyp.Service.AuthenticationService;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam("email") String email, Model model) {
        try {
            
            model.addAttribute("message", authenticationService.GeneratePasswordResetEmail(email));
        } catch (FirebaseAuthException e) {
            model.addAttribute("error", "Failed to send password reset email: " + e.getMessage());
        }
        return "forgotPassword";
    }
}
