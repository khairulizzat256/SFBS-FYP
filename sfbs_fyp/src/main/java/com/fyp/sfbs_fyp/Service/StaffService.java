package com.fyp.sfbs_fyp.Service;

import com.fyp.sfbs_fyp.Model.Staff;
import org.springframework.stereotype.Service;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;

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
}

