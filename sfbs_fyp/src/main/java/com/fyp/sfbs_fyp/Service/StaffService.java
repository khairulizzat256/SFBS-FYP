package com.fyp.sfbs_fyp.Service;

import com.fyp.sfbs_fyp.Model.Staff;
import org.springframework.stereotype.Service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.DocumentReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.cloud.FirestoreClient;

import java.util.ArrayList;
import java.util.List;



@Service
public class StaffService {

    //Get Staff List
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
        // for (int j = 0; j < staffList.size(); j++) {
        //     System.out.println("\n\n Staff Service" + j+ ". " + staffList.get(j).getStaffEmail());
        // }
        return staffList;
    }

    //Get Staff
     public Staff getStaff(String staffID) throws FirebaseAuthException {
        
        Staff staff = new Staff();
        UserRecord userRecord = FirebaseAuth.getInstance().getUser(staffID);
        staff.setStaffID(userRecord.getUid());
        staff.setStaffEmail(userRecord.getEmail());
        staff.setStaffName(userRecord.getDisplayName());
        staff.setStaffPhone(userRecord.getPhoneNumber());
        
        return staff;
     }

     //Save Staff
     public void saveStaff(Staff staff) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
            .setEmail(staff.getStaffEmail())
            .setDisplayName(staff.getStaffName())
            .setPhoneNumber(staff.getStaffPhone())
            .setPassword("Admin@123")
            .setDisabled(false);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        staff.setStaffID(userRecord.getUid());  
        
     }
     //Delete Staff
        public void deleteStaff(String staffID) throws FirebaseAuthException {
            FirebaseAuth.getInstance().deleteUser(staffID);
        }


    }

