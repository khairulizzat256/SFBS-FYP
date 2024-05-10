package com.fyp.sfbs_fyp.Service;

import com.fyp.sfbs_fyp.Model.Facility;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;



public class FacilityService {
    public Facility retrieveFacilityData(Facility facility) {
        // Get Firestore instance using Firebase Admin SDK
        Firestore firestore = FirestoreClient.getFirestore();

        // Retrieve facility data from Firestore
        try {
            // Get the "facility" collection
            CollectionReference collection = firestore.collection("facility");

            // Get all documents in the "facility" collection
            ApiFuture<QuerySnapshot> query = collection.get();
            QuerySnapshot querySnapshot = query.get();

            // Iterate through all documents in the "facility" collection
            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                // Get the facility data from the document
                Facility facilityData = document.toObject(Facility.class);

                // Check if the facilityID matches the facilityID provided
                if (facilityData.getFacilityID().equals(facility.getFacilityID())) {
                    // Return the facility data if the facilityID matches
                    return facilityData;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("/n/n COULD NOT RETRIEVE FROM FIRESTORE/n/n");
            // Handle the exception appropriately
        }

        // Return null if the facilityID does not match any facility in the "facility" collection
        return null;
    }
}
