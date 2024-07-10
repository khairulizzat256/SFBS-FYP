package com.fyp.sfbs_fyp.Controller;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.checkerframework.checker.units.qual.m;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fyp.sfbs_fyp.Service.StaffService;
import com.fyp.sfbs_fyp.Service.ReportService;
import com.fyp.sfbs_fyp.Model.Booking;
import com.fyp.sfbs_fyp.Model.Staff;
import com.fyp.sfbs_fyp.Service.AuthenticationService;
import com.fyp.sfbs_fyp.Service.BookingService;
import com.fyp.sfbs_fyp.Service.FacilityService;
import com.google.firebase.auth.FirebaseAuthException;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;


@Controller
@RequestMapping("/Admin")
public class AdminController {

    @Autowired
    private AuthenticationService user;
    @Autowired
    private StaffService staffService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private FacilityService FacilityService;
    @Autowired
    private ReportService ReportService;
    
    // Admin dashboard
    @GetMapping("/dashboard")
    public String dashboard(@RequestParam("uid") String uid, Model model, HttpSession session) throws FirebaseAuthException, InterruptedException, ExecutionException, IOException {

        
        // Check User ID Authentication
        // Assume user.Authentication(uid); is handled elsewhere
        session.setAttribute("user", staffService.getStaff(uid));
        
        // Get Staff List
        model.addAttribute("staffList", staffService.getStaffList());
        // Get Booking List
        model.addAttribute("bookingList", bookingService.retrieveBookingList());

        // Get Image
        byte[] imageBytes = bookingService.fetchImage("booking1720629716189");
        String base64Image = Base64.getEncoder().encodeToString(imageBytes);
        model.addAttribute("image", base64Image);
                
        
        // Get Facility List
        model.addAttribute("facilityList", FacilityService.retrieveFacilityList());
        // Generate and Get Report List
        ReportService.generateReport(staffService.getStaff(uid)); // Ensure reports are generated
        model.addAttribute("reportList", ReportService.retrieveReportList());
    
        // Get all bookings and pass them as a map to the model
        List<Booking> allBookings = bookingService.retrieveBookingList();
        Map<String, Booking> bookingsMap = allBookings.stream().collect(Collectors.toMap(Booking::getBookingID, booking -> booking));
        model.addAttribute("bookings", bookingsMap);

        return "AdminDashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) { 
        user.logout(session);
        
        return "redirect:/login";
    }

    @GetMapping("/fetchImage")
    public ResponseEntity<byte[]> fetchImage(@RequestParam("bookingID") String bookingID) throws IOException {
        byte[] imageData = bookingService.fetchImage(bookingID);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
    }
    

    @GetMapping("/editStaff")
    public String editStaff(@RequestParam("uid") String staffID, Model model) throws FirebaseAuthException {

        model.addAttribute("staff", staffService.getStaff(staffID));
        return "EditStaff";
    }

    @GetMapping("/AddStaff")
    public String AddStaff(HttpSession session) throws FirebaseAuthException {
        Staff user = (Staff) session.getAttribute("user");
        String uid = user.getStaffID();
        return "addstaff";
    }

    @RequestMapping("/SaveStaff")
    public String SaveStaff(@RequestParam("staffName") String staffName,
                            @RequestParam("staffEmail") String staffEmail,
                            @RequestParam("staffPhone") String staffPhone,
                            @RequestParam("staffRole") String staffRole,
                            HttpSession session,
                            Model model) throws FirebaseAuthException {

        Staff newStaff = new Staff("", staffName, staffEmail, staffPhone, staffRole);
        Staff user = (Staff) session.getAttribute("user");
        String uid = user.getStaffID();

        // Check existing user
        String existingUserMessage = staffService.checkExistingUser(newStaff);
        if (existingUserMessage != null && !existingUserMessage.isEmpty()) {
            model.addAttribute("message", existingUserMessage);
            return "addstaff"; // Return to the same page to display the message
        }

        staffService.saveStaff(newStaff);
        return "redirect:/Admin/dashboard?uid=" + uid;
    }

    @GetMapping("/deleteStaff")
    public String deleteStaff(@RequestParam("uid") String staffID, Model model, HttpSession session) throws FirebaseAuthException {
        Staff user = (Staff) session.getAttribute("user");
        String uid = user.getStaffID();

        staffService.deleteStaff(staffID);
        
        return "redirect:/Admin/dashboard?uid=" + uid;
    }

    @GetMapping("/cancelBooking")
    public String cancelBooking(@RequestParam("bookingID") String bookingID, Model model, HttpSession session) throws FirebaseAuthException, InterruptedException, ExecutionException {
        Booking booking = bookingService.retrieveBookingData(bookingID);
        bookingService.cancelBooking(booking);

        Staff user = (Staff) session.getAttribute("user");
        String uid = user.getStaffID();
       
        
        return "redirect:/Admin/dashboard?uid="+ uid;
    }

    
    @GetMapping("/CheckInBooking")
    public String CheckInBooking(@RequestParam("bookingID") String bookingID, Model model, HttpSession session) throws FirebaseAuthException, InterruptedException, ExecutionException {
        Booking booking = bookingService.retrieveBookingData(bookingID);
        bookingService.CheckInBooking(booking);
        Staff user = (Staff) session.getAttribute("user");
        String uid = user.getStaffID();
    
        return "redirect:/Admin/dashboard?uid="+ uid;
    }

    @GetMapping("/PaidBooking")
    public String PaidBooking(@RequestParam("bookingID") String bookingID, Model model, HttpSession session) throws FirebaseAuthException, InterruptedException, ExecutionException {
        Booking booking = bookingService.retrieveBookingData(bookingID);
        bookingService.PaidBooking(booking);
        Staff user = (Staff) session.getAttribute("user");
        String uid = user.getStaffID();
    
        return "redirect:/Admin/dashboard?uid="+ uid;
    }

    @GetMapping("/CheckOutBooking")
    public String CompletedBooking(@RequestParam("bookingID") String bookingID, Model model, HttpSession session) throws FirebaseAuthException, InterruptedException, ExecutionException {
        Booking booking = bookingService.retrieveBookingData(bookingID);
        bookingService.CompleteBooking(booking);
        Staff user = (Staff) session.getAttribute("user");
        String uid = user.getStaffID();
    
        return "redirect:/Admin/dashboard?uid="+ uid;
    }
    
    @GetMapping("/forgotPassword")
    public String forgotPasswordForm() {
        return "forgotPassword";
    }
    
}
