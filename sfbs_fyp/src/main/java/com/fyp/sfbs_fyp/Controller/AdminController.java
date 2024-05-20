package com.fyp.sfbs_fyp.Controller;

import org.checkerframework.checker.units.qual.s;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fyp.sfbs_fyp.Service.StaffService;
import com.fyp.sfbs_fyp.Model.Staff;
import com.fyp.sfbs_fyp.Service.AuthenticationService;

import com.google.firebase.auth.FirebaseAuthException;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;


@Controller
@RequestMapping("/Admin")
public class AdminController {
    AuthenticationService user = new AuthenticationService();
    
    // Admin dashboard
    @GetMapping("/dashboard")
    public String dashboard(@RequestParam("uid") String uid, Model model, HttpSession session) throws FirebaseAuthException{    
        
        //Check User ID Authentication
        user.Authentication(uid);
        session.setAttribute("user",uid);

        System.out.println(session.getAttribute("uid"));
        
        //Get Staff List
        StaffService staffService = new StaffService();
        model.addAttribute("staffList", staffService.getStaffList());
       
        //Get Booking List
        //TODO: Get Booking List
        //Get Report List
        //TODO: Get Report List

        return "AdminDashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) { 
        user.logout(session);
        
        return "redirect:/login";
    }
    

    @GetMapping("/editStaff")
    public String editStaff(@RequestParam("uid") String staffID, Model model) throws FirebaseAuthException {
        StaffService staffService = new StaffService();
        model.addAttribute("staff", staffService.getStaff(staffID));
        return "EditStaff";
    }

    @GetMapping("/AddStaff")
    public String AddStaff(HttpSession session) throws FirebaseAuthException {
        //model.addAttribute("staff", staffService.getStaff(staffID));
        return "addstaff";
    }

    @RequestMapping("/SaveStaff")
    public String SaveStaff(@RequestParam("staffName") String staffName,
                            @RequestParam("staffEmail") String staffemail,
                            @RequestParam("staffPhone") String staffPhone,
                            @RequestParam("staffRole") String staffRole,
                            HttpSession session) throws FirebaseAuthException {

        StaffService staffService = new StaffService();
        Staff newstaff = new Staff("", staffName, staffemail, staffPhone, staffRole);
        System.out.println("\n\n\nSAVE DATA\n\n\n");
        staffService.saveStaff(newstaff);
        //model.addAttribute("staff", staffService.getStaff(staffID));
        return "redirect:/Admin/dashboard?uid=" + session.getAttribute("user");
    }

    @GetMapping("/deleteStaff")
    public String deleteStaff(@RequestParam("uid") String staffID, Model model, HttpSession session) throws FirebaseAuthException {
        StaffService staffService = new StaffService();
        staffService.deleteStaff(staffID);
        return "redirect:/Admin/dashboard?uid=" + session.getAttribute("user");
    }
}
