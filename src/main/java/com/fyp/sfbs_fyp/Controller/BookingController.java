package com.fyp.sfbs_fyp.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;

import com.google.cloud.storage.Storage;

import com.fyp.sfbs_fyp.Model.Booking;
import com.fyp.sfbs_fyp.Model.Company;
import com.fyp.sfbs_fyp.Model.Customer;
import com.fyp.sfbs_fyp.Model.Facility;
import com.fyp.sfbs_fyp.Service.BookingService;
import com.fyp.sfbs_fyp.Service.CustomerService;
import com.fyp.sfbs_fyp.Service.FacilityService;


@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    BookingService bookingService;
    @Autowired
    CustomerService customerService;
    @Autowired
    FacilityService facilityService;
    @Autowired
    private Storage storage;

    @GetMapping("/{bookingID}")
public String viewBookingDetails(@PathVariable("bookingID") String bookingID, Model model) {
    try {
        Booking booking = bookingService.retrieveBookingData(bookingID);
        
        if (booking != null) {
            if ("Paid".equals(booking.getPaymentStatus())) {
                byte[] imageBytes = bookingService.fetchImage(bookingID);
                if (imageBytes != null && imageBytes.length > 0) {
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    model.addAttribute("image", base64Image);
                } else {
                    model.addAttribute("image", null);
                }
            } else {
                model.addAttribute("image", null);
            }
            model.addAttribute("booking", booking);
            return "bookingDetails";
        } else {
            model.addAttribute("message", "Booking not found");
            return "error";
        }
    } catch (Exception e) {
        model.addAttribute("message", "An error occurred: " + e.getMessage());
        return "error";
    }
}



    @PostMapping("/confirmbooking")
    public String confirmBooking(@RequestBody Map<String, Object> bookingData, Model model) throws InterruptedException, ExecutionException {
        // Extract customer details
        Customer customer = new Customer();
        customer.setCustomerName((String) bookingData.get("customerName"));
        customer.setCustomerPhone((String) bookingData.get("customerPhone"));
        customer.setCustomerEmail((String) bookingData.get("customerEmail"));

        // Extract booking details
        Facility facility = new Facility();

        facility = facilityService.getFacility((String) bookingData.get("facility"));

        Booking booking = new Booking();
        booking.setFacilityID(facility);
        booking.setBookingType((String) bookingData.get("bookingType"));

        // Generate booking ID (example, replace with your logic)
        booking.setBookingID("booking" + new Date().getTime());
        System.out.println("Booking ID SET: " + booking.getBookingID());
       
        booking.setBookingDate((String) bookingData.get("bookingDate"));
      
        booking.setBookingStartTime((String) bookingData.get("bookingStartTime"));
        booking.setBookingEndTime((String) bookingData.get("bookingEndTime"));
        booking.setStatus("Reserved");
        booking.setpaymentType((String) bookingData.get("paymentType"));

        //get the total amount by multiplying the facility price with the duration
        double totalAmount = facility.getFacilityPrice() * bookingService.GetDuration(booking.getBookingStartTime(), booking.getBookingEndTime());

        booking.setTotalamount(totalAmount);
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

        }

        booking.setCustomerID(customer);

        // Check if booking date and time is available
        if(!bookingService.ValidateDateTime(booking.getBookingDate(), booking.getBookingStartTime(), booking.getBookingEndTime(), booking.getFacilityID().getFacilityID())) {
            model.addAttribute("message", "Booking date and time is not available");
            return "redirect:/booking?message=Booking date and time is not available";
        }

        // Add booking details to model
        model.addAttribute("booking", booking);
        System.out.println(booking.toString());
        //print all company details

        return "confirmBooking";
    }
    @PostMapping("/savebooking")
    public String saveBooking(@ModelAttribute Booking booking, @RequestParam(value = "paymentProof", required = false) MultipartFile file, Model model) {
        try {
            try {
                if ("Online Transfer/QR".equals(booking.getpaymentType()) && file != null && !file.isEmpty()) {
                    System.out.println("File Name: " + file.getOriginalFilename());
                    String fileName = "paymentProof/" + booking.getBookingID() + ".jpg";
                    String bucketName = "sfbs-19116.appspot.com";
                    BlobId blobId = BlobId.of(bucketName, fileName);
                    BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                            .setContentType("image/" + getFileExtension(file.getOriginalFilename()))
                            .build();
                    storage.create(blobInfo, file.getBytes());
                    
                    // Get the URL of the uploaded file
                    @SuppressWarnings("unused")
                    String fileUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
                    booking.setPaymentStatus("Paid");
                    // Store the file URL in Firestore
                // booking.setPaymentProof(fileUrl);
                } else {
                    booking.setPaymentStatus("Unpaid");
                    // If payment type is 'Pay at Counter', do not set payment proof
                    //booking.setPaymentProof(null);
                }
                
            } catch (Exception e) {
                e.printStackTrace();
                String message = "Failed to save payment proof: " + e.getMessage();
                model.addAttribute("message", message);
                return "redirect:/booking?message="+message;
            }
            // Save customer to Firestore if not exists
            try {
                if (!customerService.isUserExist(booking.getCustomerID().getCustomerEmail())) {
                    booking.getCustomerID().setCustomerID(customerService.generateCustomerID());
                    customerService.saveCustomer(booking.getCustomerID());
                }
                booking.getCustomerID().setCustomerID(customerService.getUID(booking.getCustomerID().getCustomerEmail()));
            } catch (Exception e) {
                e.printStackTrace();
                String message = "Failed to save customer: Invalid email and phone number";
                model.addAttribute("message", message);
                return "redirect:/booking?message="+message;
            }
            // Save booking to Firestore
            bookingService.saveBooking(booking);

            // Trigger email sending
            bookingService.sendEmailTrigger(booking);
            model.addAttribute("success", "Booking successfully saved");
            return "redirect:/booking?message=Booking successfully saved";

        } catch (Exception e) {
            e.printStackTrace();
            String message = "Failed to save booking:" + e.getMessage();
            model.addAttribute("message", message);
            return "redirect:/booking?message="+message;
        }
    }

    @PostMapping("/cancelbooking")
    public String cancelBooking(@RequestParam("bookingID") String bookingID, @RequestParam("description") String description, Model model) {
        try {
            bookingService.cancelBooking(bookingID, description);
            return "redirect:/booking/"+bookingID;
        } catch (Exception e) {
            
            return "redirect:/booking/"+bookingID;
        }
    }
    


    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        int dotIndex = fileName.lastIndexOf('.');
        return dotIndex == -1 ? "" : fileName.substring(dotIndex + 1);
    }

    @GetMapping("/events")
    @ResponseBody
    public List<Map<String, Object>> getBookingEvents() throws InterruptedException, ExecutionException {
        return bookingService.getBookingEvents();
    }



}
