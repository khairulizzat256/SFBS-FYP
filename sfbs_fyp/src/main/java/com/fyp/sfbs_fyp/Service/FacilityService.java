package com.fyp.sfbs_fyp.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fyp.sfbs_fyp.Model.Facility;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;


@Service
public class FacilityService {
    
    //Get facility by facilityName
    public Facility getFacility(String facilityName) {
        // Get Firestore instance using Firebase Admin SDK
        Firestore firestore = FirestoreClient.getFirestore();
    
        // Create a facility object to store the facility data
        Facility facility = new Facility();
    
        // Retrieve facility data from Firestore
        try {
            // Get the "facility" collection
            CollectionReference collection = firestore.collection("Facility");
    
            // Get the document with the specified facilityName
            ApiFuture<QuerySnapshot> query = collection.whereEqualTo("facilityName", facilityName).get();
            QuerySnapshot querySnapshot = query.get();
    
            // Get the facility data from the document
            facility = querySnapshot.getDocuments().get(0).toObject(Facility.class);
    
            // Set the facilityID from the document ID
            facility.setFacilityID(querySnapshot.getDocuments().get(0).getId());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\n\nCOULD NOT RETRIEVE FROM FIRESTORE\n\n");
            // Handle the exception appropriately
        }
    
        // Return the facility data
        return facility;
    }



    public List<Facility> retrieveFacilityList() {
        // Get Firestore instance using Firebase Admin SDK
        Firestore firestore = FirestoreClient.getFirestore();
    
        // Create a list to store the facility data
        List<Facility> facilityList = new ArrayList<>();
    
        // Retrieve facility data from Firestore
        try {
            // Get the "facility" collection
            CollectionReference collection = firestore.collection("Facility");
    
            // Get all documents in the "facility" collection
            ApiFuture<QuerySnapshot> query = collection.get();
            QuerySnapshot querySnapshot = query.get();
    
            // Iterate through all documents in the "facility" collection
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                // Get the facility data from the document
                Facility facilityData = document.toObject(Facility.class);
                
                // Set the facilityID from the document ID
                facilityData.setFacilityID(document.getId());
    
                // Add the facility data to the list
                facilityList.add(facilityData);
    
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("\n\nCOULD NOT RETRIEVE FROM FIRESTORE\n\n");
            // Handle the exception appropriately
        }
    
        // Return the list of facility data
        return facilityList;
    }
    
}
