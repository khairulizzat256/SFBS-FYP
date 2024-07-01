package com.fyp.sfbs_fyp.Service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import com.google.firebase.auth.UserRecord;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Service;



@Service
public class AuthenticationService {

    public UserRecord Authentication(String uid) throws FirebaseAuthException{
        // Check if user is logged in using Firebase Auth
        UserRecord user = null;
        FirebaseAuth auth = FirebaseAuth.getInstance();
        try {
            user = auth.getUser(uid);
            System.out.println("\n\nUser  " + user.getEmail() + " with UID: " + user.getUid() + " is logged in.\n\n" + user.getTokensValidAfterTimestamp() + "\n\n");
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }
    
    public String GeneratePasswordResetEmail(String email) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().generatePasswordResetLink(email);
        
    }


    public String logout(HttpSession session) {
        session.invalidate();
        System.out.println("LOUGOUT" + session); // Invalidate the session
        return "Logout Successfull";
    }
}

