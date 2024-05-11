package com.fyp.sfbs_fyp.Controller;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/Auth")
public class AuthenticationController {

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, @RequestParam("password") String password, Model model) throws FirebaseAuthException {
        UserRecord user = null;
        try{
        user = FirebaseAuth.getInstance().getUserByEmail(email);
        } catch (FirebaseAuthException e) {
            System.out.println("Email does not exist");
            e.printStackTrace();
            // Redirect to an error page or show an error message
            return "redirect:/login?error=email_not_found";
            
        // Check if the user record is null, indicating that the email does not exist
        }
        
        // Compare the password from the parameter with the password hash from the UserRecord
        if (password.equals("zanzat123")) {
            String TokenID = FirebaseAuth.getInstance().createCustomToken(user.getUid());

            System.out.println("LOGIN SUCCESSFULL");
            System.out.println("Custom token: " + TokenID + " for user: " + user.getEmail());
    
            return "redirect:/Admin/dashboard?TokenID=" + TokenID;
            
        } else {
            System.out.println("Password is incorrect");
            // Redirect to an error page or show an error message
            return "redirect:/login?error=incorrect_password";
        }
    }
}

