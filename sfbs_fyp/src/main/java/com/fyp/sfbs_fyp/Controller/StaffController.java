package com.fyp.sfbs_fyp.Controller;

import com.fyp.sfbs_fyp.Model.Staff;
import com.fyp.sfbs_fyp.Service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StaffController {

    private final StaffService staffService;

    @Autowired
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    @PostMapping("/saveStaff")
    public String saveStaff(Staff staff) {
        staffService.saveStaff(staff);
        return "redirect:/success"; // Redirect to a success page after saving staff
    }
}
