package com.fyp.sfbs_fyp.Service;

import com.fyp.sfbs_fyp.Model.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class StaffService {

    @Autowired
    Firestore firestore;

    // Get Staff List
    public List<Staff> getStaffList() throws FirebaseAuthException {
        List<Staff> staffList = new ArrayList<>();

        ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);
       
        while (page != null) {
            for (UserRecord userRecord : page.getValues()) {
                Staff staff = new Staff();
                staff.setStaffID(userRecord.getUid());
                staff.setStaffEmail(userRecord.getEmail());
                staff.setStaffName(userRecord.getDisplayName());
                staff.setStaffPhone(userRecord.getPhoneNumber());
                
                staffList.add(staff);
            }
            page = page.getNextPage();
        }
        return staffList;
    }

    // Get Staff
    public Staff getStaff(String staffID) throws FirebaseAuthException {
        Staff staff = new Staff();
        UserRecord userRecord = FirebaseAuth.getInstance().getUser(staffID);
        staff.setStaffID(userRecord.getUid());
        staff.setStaffEmail(userRecord.getEmail());
        staff.setStaffName(userRecord.getDisplayName());
        staff.setStaffPhone(userRecord.getPhoneNumber());
        
        return staff;
    }

    // Save Staff
    public void saveStaff(Staff staff) throws FirebaseAuthException {
        
        List<String> existingUIDs = getAllUIDs();
        String customUID = generateCustomUID(staff.getStaffRole(), existingUIDs);

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
            .setUid(customUID)
            .setEmail(staff.getStaffEmail())
            .setDisplayName(staff.getStaffName())
            .setPhoneNumber(staff.getStaffPhone())
            .setPassword("Admin@123")
            .setDisabled(false);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        staff.setStaffID(userRecord.getUid());
        saveStaffInFirestore(staff);
    }
    // Save Staff in Firestore
    private void saveStaffInFirestore(Staff staff) {
        Firestore firestore = FirestoreClient.getFirestore();
        try {
            DocumentReference docRef = firestore.collection("Staff").document(staff.getStaffID());
            ApiFuture<WriteResult> future = docRef.set(staff);

            // Add a listener to be notified when the future is completed
            future.addListener(() -> {
                try {
                    WriteResult result = future.get();
                    System.out.println("Staff successfully written at: " + result.getUpdateTime());
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println("Error writing document: " + e);
                }
            }, MoreExecutors.directExecutor());
            
        } catch (Exception e) {
            System.err.println("Error writing document: " + e);
        }
    }

    private String generateCustomUID(String role, List<String> existingUIDs) {
        String prefix = role.equals("Admin") ? "A" : "M";
        String year = String.valueOf(java.time.Year.now().getValue());
        int number = 0;
        String uid;

        do {
            uid = prefix + year + String.format("%03d", number);
            number++;
        } while (existingUIDs.contains(uid));

        return uid;
    }

    private List<String> getAllUIDs() throws FirebaseAuthException {
        List<String> uids = new ArrayList<>();
        ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);
        while (page != null) {
            for (UserRecord userRecord : page.getValues()) {
                uids.add(userRecord.getUid());
            }
            page = page.getNextPage();
        }
        return uids;
    }

    // Delete Staff
    public void deleteStaff(String staffID) throws FirebaseAuthException {
        FirebaseAuth.getInstance().deleteUser(staffID);
        // Delete staff from Firestore
        Firestore firestore = FirestoreClient.getFirestore();
        firestore.collection("Staff").document(staffID).delete();
    }
}
