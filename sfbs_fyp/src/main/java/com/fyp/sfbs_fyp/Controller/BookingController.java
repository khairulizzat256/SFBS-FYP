package com.fyp.sfbs_fyp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.Time;
import java.util.Map;

import com.fyp.sfbs_fyp.Model.Booking;
import com.fyp.sfbs_fyp.Model.Customer;
import com.fyp.sfbs_fyp.Model.Facility;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;



@Controller
@RequestMapping("/booking")
public class BookingController {

    //   @Autowired
    //   Firestore firestore;

    @PostMapping
    public Map<String, Object> saveBooking(@RequestBody Map<String, Object> bookingData) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        try {
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
            booking.setBookingDate((Date) bookingData.get("bookingDate"));
            booking.setBookingStartTime((Time) bookingData.get("bookingTime"));
            booking.setCustomerID(customer);

            // Save booking to Firestore
             DocumentReference docRef = dbFirestore.collection("Bookings").document();
             docRef.set(booking);

            // Return success message
            return Map.of("success", true, "message", "Booking successfully saved");
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("success", false, "message", "Failed to save booking");
        }
    }
}
