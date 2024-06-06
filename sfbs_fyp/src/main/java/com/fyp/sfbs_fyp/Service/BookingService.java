package com.fyp.sfbs_fyp.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.fyp.sfbs_fyp.Model.Booking;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;

@Service
public class BookingService {
    
    public void saveBooking(Booking booking) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        // Save booking to database
        DocumentReference docRef = dbFirestore.collection("Booking").document(booking.getBookingID());
            docRef.set(booking);
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
        List<Booking> bookingList = new ArrayList<Booking>();
        for (QueryDocumentSnapshot document : querySnapshot) {
            bookingList.add(document.toObject(Booking.class));
        }
        return bookingList;
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
            if (bookingData.getFacilityID().getFacilityID().equals(facilityID)) {
                if (bookingData.getBookingDate().equals(bookingDate)) {
                    if (bookingData.getBookingStartTime().equals(bookingStartTime) || bookingData.getBookingEndTime().equals(bookingEndTime)) {
                        return false;
                    }
                }
            }
        }

        //Check if the booking date and time not passed
        if (bookingDate.compareTo(java.time.LocalDate.now().toString()) < 0) {
            return false;
        }

        //Check if the booking start time and end time is not the same or the start time is not later than the end time
        if(bookingStartTime.compareTo(bookingEndTime) >= 0) {
            return false;
        }

        //Check if the start time and end time is available (08:00 - 22:00)
        if(bookingStartTime.compareTo("08:00") < 0 || bookingEndTime.compareTo("22:00") > 0) {
            return false;
        }

        return true;
    }

    //Get the event, date, start time and end time of the booking
    public List<Map<String, Object>> getBookingEvents() throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference booking = dbFirestore.collection("Booking");
        ApiFuture<QuerySnapshot> query = booking.get();
        QuerySnapshot querySnapshot = query.get();
        List<Map<String, Object>> events = new ArrayList<>();

        for (QueryDocumentSnapshot document : querySnapshot) {
            Booking bookingData = document.toObject(Booking.class);
            Map<String, Object> event = new HashMap<>();
            event.put("title", bookingData.getFacilityID().getFacilityName() + ": " + bookingData.getCustomerID().getCustomerName());
            event.put("start", bookingData.getBookingDate() + "T" + bookingData.getBookingStartTime());
            event.put("end", bookingData.getBookingDate() + "T" + bookingData.getBookingEndTime());
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
}
