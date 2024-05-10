package com.fyp.sfbs_fyp.Service;

import com.fyp.sfbs_fyp.Model.Staff;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class StaffService {

    public void saveStaff(Staff staff) {
        // Get Firestore instance using Firebase Admin SDK
        Firestore firestore = FirestoreClient.getFirestore();

        // Save staff data to Firestore
        try {
            // You may want to generate a unique ID for the document if staffID is not provided
            // String staffId = UUID.randomUUID().toString();
            // staff.setStaffID(staffId);

            // Set the staff document in the "staff" collection with the staffID as the document ID
            ApiFuture<WriteResult> result = firestore.collection("staff").document(staff.getStaffID()).set(staff);
            
            // Log the update time
            System.out.println("Update time: " + result.get().getUpdateTime());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.out.println("/n/n COULD NOT SAVE TO FIRESTORE/n/n");
            // Handle the exception appropriately
        }
    }
}

