package com.fyp.sfbs_fyp.Service;

import java.util.ArrayList;
import java.util.List;
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
    public boolean compareDateTime(String bookingDate, String bookingStartTime, String bookingEndTime, String facilityID) throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        CollectionReference booking = dbFirestore.collection("Booking");
        ApiFuture<QuerySnapshot> query = booking.get();
        QuerySnapshot querySnapshot = query.get();
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
        return true;
    }


}
