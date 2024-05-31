package com.fyp.sfbs_fyp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import com.fyp.sfbs_fyp.Model.Booking;
import com.fyp.sfbs_fyp.Model.Company;
import com.fyp.sfbs_fyp.Model.Customer;
import com.fyp.sfbs_fyp.Model.Facility;
import com.fyp.sfbs_fyp.Service.BookingService;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    BookingService bookingService;

   
    
    @PostMapping("/confirmbooking")
    public String confirmBooking(@RequestBody Map<String, Object> bookingData, Model model) throws InterruptedException, ExecutionException {
        // Extract customer details
        Customer customer = new Customer();
        customer.setCustomerName((String) bookingData.get("customerName"));
        customer.setCustomerPhone((String) bookingData.get("customerPhone"));
        customer.setCustomerEmail((String) bookingData.get("customerEmail"));

        // Extract booking details
        Facility facility = new Facility();
        facility.setFacilityID((String) bookingData.get("facility"));

        Booking booking = new Booking();
        booking.setFacilityID(facility);
        booking.setBookingType((String) bookingData.get("bookingType"));

        // Generate booking ID (example, replace with your logic)
        booking.setBookingID("booking" + new Date().getTime());
        System.out.println("Booking ID SET: " + booking.getBookingID());
        // Set booking date to the selected date
        // String bookingDateString = (String) bookingData.get("bookingDate");
        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // Date bookingDate = dateFormat.parse(bookingDateString);
        booking.setBookingDate((String) bookingData.get("bookingDate"));
        
        // Parse the booking start and end times
        // SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        // Date bookingStartTime = timeFormat.parse((String) bookingData.get("bookingStartTime"));
        // Date bookingEndTime = timeFormat.parse((String) bookingData.get("bookingEndTime"));
        //set the bookingstarttime and bookingendtime date same with bookingdate
      
        booking.setBookingStartTime((String) bookingData.get("bookingStartTime"));
        booking.setBookingEndTime((String) bookingData.get("bookingEndTime"));

        // Include company details if booking type is "Company"
        if ("Company".equals(bookingData.get("bookingType"))) {
            Company company = new Company(
                "Company001",
                (String) bookingData.get("companyName"),
                (String) bookingData.get("companyAddress"),
                (String) bookingData.get("eventName"),
                (String) bookingData.get("additionalService")
            );
            booking.setCompanyID(company);

            //CHECK COMPANY DETAILS
            // System.out.println("Company ID: "   + booking.getCompanyID().getCompanyID() 
            //                                     + "\nCompany Name: " + booking.getCompanyID().getCompanyName() 
            //                                     + "\nCompany Address: " + booking.getCompanyID().getCompanyAddress() 
            //                                     + "\nEvent Name: " + booking.getCompanyID().getEventName() 
            //                                     + "\nService: " + booking.getCompanyID().getService() 
            //                                     + "\n");
        }

        booking.setCustomerID(customer);

        // Check if booking date and time is available
        if(!bookingService.compareDateTime(booking.getBookingDate(), booking.getBookingStartTime(), booking.getBookingEndTime(), booking.getFacilityID().getFacilityID())) {
            model.addAttribute("message", "Booking date and time is not available");
            return "redirect:/booking?message=Booking date and time is not available";
        }

        // Add booking details to model
        model.addAttribute("booking", booking);
        //System.out.println(booking.toString());
        //print all company details

        return "confirmBooking";
    }
    @PostMapping("/savebooking")
    public String saveBooking(@ModelAttribute Booking booking, Model model) {
       
        try {
            // Save booking to Firestore
            bookingService.saveBooking(booking);
            model.addAttribute("success", "Booking successfully saved");
            return "redirect:/booking?message=Booking successfully saved";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Failed to save booking: " + e.getMessage());
           return "redirect:/booking";
        }
    }



}
