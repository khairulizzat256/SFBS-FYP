package com.fyp.sfbs_fyp.Service;

import org.springframework.stereotype.Service;

import com.fyp.sfbs_fyp.Model.Customer;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    // To save customer detail in Firestore
    public void saveCustomer(Customer customer) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        try {
            
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setUid(customer.getCustomerID())
                    .setEmail(customer.getCustomerEmail())
                    .setEmailVerified(false)
                    .setDisplayName(customer.getCustomerName())
                    .setPhoneNumber(customer.getCustomerPhone())
                    .setDisabled(false);
            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
            DocumentReference docRef = dbFirestore.collection("Customer").document(customer.getCustomerID());
            docRef.set(customer);
            System.out.println("Successfully created new user: " + userRecord.getUid());
        } catch (FirebaseAuthException e) {
            System.out.println("Error creating new user: " + e.getMessage());
        }
    }

    // Validate if the user exists in Firebase Authentication
    public boolean isUserExist(String email) {
        try {
            FirebaseAuth.getInstance().getUserByEmail(email);
            return true;
        } catch (FirebaseAuthException e) {
            return false;
        }
    }

    // If the user email exists, return the UID
    public String getUID(String email) {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(email);
            return userRecord.getUid();
        } catch (FirebaseAuthException e) {
            return null;
        }
    }

    // Get a list of users from Firebase Authentication
    public List<UserRecord> getUserList() {
        List<UserRecord> users = new ArrayList<>();
        try {
            ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);
            while (page != null) {
                for (UserRecord user : page.getValues()) {
                    users.add(user);
                }
                page = page.getNextPage();
            }
        } catch (FirebaseAuthException e) {
            System.out.println("Error retrieving user list: " + e.getMessage());
        }
        return users;
    }

    // Generate customer ID as C[year][3 digit number]
    public String generateCustomerID() {
        String prefix = "C";
        String year = String.valueOf(java.time.Year.now().getValue());
        int number = 0;
        String customerID;

        List<UserRecord> users = getUserList();
        List<String> existingIDs = new ArrayList<>();
        for (UserRecord user : users) {
            if (user.getUid().startsWith(prefix + year)) {
                existingIDs.add(user.getUid());
            }
        }

        do {
            customerID = prefix + year + String.format("%03d", number);
            number++;
        } while (existingIDs.contains(customerID));

        return customerID;
    }
   
}
