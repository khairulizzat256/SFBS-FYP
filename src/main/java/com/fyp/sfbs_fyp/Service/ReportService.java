package com.fyp.sfbs_fyp.Service;

import com.fyp.sfbs_fyp.Model.Booking;
import com.fyp.sfbs_fyp.Model.Report;
import com.fyp.sfbs_fyp.Model.Staff;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class ReportService {

    @Autowired
    private BookingService bookingService;

    private List<Report> reportList = new ArrayList<>();

    public Report generateReport(Staff staff) throws InterruptedException, ExecutionException {
        List<Booking> bookings = bookingService.retrieveBookingList();
        Map<Date, List<Booking>> bookingsByDate = new HashMap<>();
        SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Group bookings by date
        for (Booking booking : bookings) {
            try {
                Date bookingDate = sqlDateFormat.parse(booking.getBookingDate());
                bookingsByDate.computeIfAbsent(bookingDate, k -> new ArrayList<>()).add(booking);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Generate reports
        List<Report> generatedReports = new ArrayList<>();
        for (Map.Entry<Date, List<Booking>> entry : bookingsByDate.entrySet()) {
            Date date = entry.getKey();
            List<Booking> dailyBookings = entry.getValue();
            double totalSales = dailyBookings.stream().mapToDouble(Booking::getTotalamount).sum();

            Report report = new Report(generateReportID(date), date, dailyBookings, staff, totalSales);
            saveReport(report);
            generatedReports.add(report);
        }

        reportList.addAll(generatedReports);
        return generatedReports.isEmpty() ? null : generatedReports.get(0);
    }

    public List<Report> retrieveReportList() throws InterruptedException, ExecutionException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        List<QueryDocumentSnapshot> documents = dbFirestore.collection("Report").get().get().getDocuments();
        List<Report> reports = new ArrayList<>();
        SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat utilDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        for (QueryDocumentSnapshot document : documents) {
            Report report = document.toObject(Report.class);
            try {
                // Convert java.sql.Date to java.util.Date
                String reportDateStr = sqlDateFormat.format(report.getReportDate());
                Date utilDate = utilDateFormat.parse(reportDateStr + "T00:00:00.000+0000");
                report.setReportDate(utilDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            reports.add(report);
        }
        reportList = reports;
        return reportList;
    }

    private int generateReportID(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return Integer.parseInt(dateFormat.format(date));
    }

    private void saveReport(Report report) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection("Report").document(String.valueOf(report.getReportID())).set(report);
    }
}
