package com.fyp.sfbs_fyp.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.util.PropertySource.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fyp.sfbs_fyp.Model.Booking;
import com.google.api.core.ApiFuture;
import com.google.api.gax.paging.Page;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Service
public class BookingService {
    

    private final Storage storage;

    @Autowired
    public BookingService(Storage storage) {
        this.storage = storage;

    }
    public void saveBooking(Booking booking) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        // Save booking to database
        DocumentReference docRef = dbFirestore.collection("Booking").document(booking.getBookingID());
            docRef.set(booking);
    }

    public void cancelBooking(String bookingID, String description) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference docRef = dbFirestore.collection("Booking").document(bookingID);
        
        ApiFuture<WriteResult> future = docRef.update("status", "Cancelled", "description", description);
        
        // Wait for the write result
        future.get();
    }

    //retrieve booking data by bookingID
    public Booking retrieveBookingData(String bookingID) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference docRef = dbFirestore.collection("Booking").document(bookingID);
        // Retrieve booking data from Firestore
        Booking booking = docRef.get().get().toObject(Booking.class);
        return booking;
    }

    //retrieve list booking using List
    public List<Booking> retrieveBookingList() throws InterruptedException, ExecutionException {
    Firestore dbFirestore = FirestoreClient.getFirestore();
    CollectionReference booking = dbFirestore.collection("Booking");
    ApiFuture<QuerySnapshot> query = booking.get();
    QuerySnapshot querySnapshot = query.get();
    List<Booking> bookingList = new ArrayList<>();
    
    for (QueryDocumentSnapshot document : querySnapshot) {
        bookingList.add(document.toObject(Booking.class));
    }
    
    // Sort by booking date and time in ascending order
    bookingList.sort((b1, b2) -> {
        int dateComparison = b1.getBookingDate().compareTo(b2.getBookingDate());
        if (dateComparison == 0) {
            return b1.getBookingStartTime().compareTo(b2.getBookingStartTime());
        } else {
            return dateComparison;
        }
    });
    
    return bookingList;
}

    public void sendEmailTrigger(Booking booking) throws InterruptedException, ExecutionException {
    Firestore dbFirestore = FirestoreClient.getFirestore();
    Map<String, Object> emailData = new HashMap<>();
    
    emailData.put("to", Collections.singletonList(booking.getCustomerID().getCustomerEmail()));
    
    Map<String, String> message = new HashMap<>();
    message.put("subject", "Booking Confirmation: " + booking.getBookingID());
    message.put("text", "Thank you for your booking. Please click the link below to view your booking details:\n" + 
                        "http://yourwebsite.com/booking/" + booking.getBookingID());
    message.put("html", "<p>Thank you for your booking. Please click the link below to view your booking details:</p>" +
                        "<a href=\"https://sfbs-kelabkomuniticyberjaya-a3e4db06b778.herokuapp.com/booking/" + booking.getBookingID() + "\">View Booking</a>");
    
    emailData.put("message", message);

    // Save email data to Firestore
    dbFirestore.collection("mail").add(emailData);
}

    //compare date and time of retrieved booking data with the date and time in firestore
    public boolean ValidateDateTime(String bookingDate, String bookingStartTime, String bookingEndTime, String facilityID) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference booking = dbFirestore.collection("Booking");
        ApiFuture<QuerySnapshot> query = booking.get();
        QuerySnapshot querySnapshot = query.get();
    
        // Check if the booking date and time is available
        for (QueryDocumentSnapshot document : querySnapshot) {
            Booking bookingData = document.toObject(Booking.class);
            if (bookingData.getFacilityID().getFacilityID().equals(facilityID) && "Reserved".equals(bookingData.getStatus())) {
                if (bookingData.getBookingDate().equals(bookingDate)) {
                    if (bookingData.getBookingStartTime().equals(bookingStartTime) || bookingData.getBookingEndTime().equals(bookingEndTime)) {
                        return false;
                    }
                }
            }
        }
    
        // Check if the booking date and time not passed
        if (bookingDate.compareTo(java.time.LocalDate.now().toString()) < 0) {
            return false;
        }
    
        // Check if the booking start time and end time is not the same or the start time is not later than the end time
        if (bookingStartTime.compareTo(bookingEndTime) >= 0) {
            return false;
        }
    
        // Check if the start time and end time is available (08:00 - 22:00)
        if (bookingStartTime.compareTo("08:00") < 0 || bookingEndTime.compareTo("22:00") > 0) {
            return false;
        }
    
        return true;
    }
    

    //Get the event, date, start time and end time of the booking
    public List<Map<String, Object>> getBookingEvents() throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference booking = dbFirestore.collection("Booking");
        ApiFuture<QuerySnapshot> query = booking.whereEqualTo("status", "Reserved").get();
        QuerySnapshot querySnapshot = query.get();
        List<Map<String, Object>> events = new ArrayList<>();
    
        for (QueryDocumentSnapshot document : querySnapshot) {
            Booking bookingData = document.toObject(Booking.class);
            Map<String, Object> event = new HashMap<>();
            event.put("title", bookingData.getFacilityID().getFacilityName() + ": " + bookingData.getCustomerID().getCustomerName());
            event.put("start", bookingData.getBookingDate() + "T" + bookingData.getBookingStartTime());
            event.put("end", bookingData.getBookingDate() + "T" + bookingData.getBookingEndTime());
            event.put("facility", bookingData.getFacilityID().getFacilityName()); // Add facility to event
            events.add(event);
        }
        return events;
    }
    

    public double GetDuration(String startTime, String endTime) {
        int startHour = Integer.parseInt(startTime.substring(0, 2));
        int startMinute = Integer.parseInt(startTime.substring(3, 5));
        int endHour = Integer.parseInt(endTime.substring(0, 2));
        int endMinute = Integer.parseInt(endTime.substring(3, 5));

        double duration = (endHour - startHour) + (endMinute - startMinute) / 60.0;
        return duration;
    }

    public void cancelBooking(Booking booking) {
        // Set booking status to "Cancelled"
        booking.setStatus("Cancelled");
        // Save booking to Firestore
        saveBooking(booking);
    }

    public void CheckInBooking(Booking booking) {
        //
        booking.setStatus("Occupied");
        // Save booking to Firestore
        saveBooking(booking);
    }

    public void PaidBooking(Booking booking) {
        //
        booking.setPaymentStatus("Paid");
        // Save booking to Firestore
        saveBooking(booking);
    }

    public void CompleteBooking(Booking booking) {
        //
        booking.setStatus("Completed");
        // Save booking to Firestore
        saveBooking(booking);
    }

    public byte[] fetchImage(String bookingID) throws IOException {
        BlobId blobId = BlobId.of("sfbs-19116.appspot.com", "paymentProof/" + bookingID + ".jpg");
        Blob blob = storage.get(blobId);
        if (blob == null || !blob.exists()) {
            throw new FileNotFoundException("No such object: " + blobId.getName());
        }
        return blob.getContent();
    }
}
