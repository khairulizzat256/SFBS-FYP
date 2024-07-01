package com.fyp.sfbs_fyp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Configuration
public class FirebaseInitialization {

    private Firestore firestore;
    private Storage storage;

    @PostConstruct
    public void Initialization() {
        // Initialize Firebase
        try (InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("ServiceAccountKey.json")) {

            if (serviceAccount == null) {
                throw new IOException("ServiceAccountKey.json not found");
            }
            // Initialize Firebase
            @SuppressWarnings("deprecation")
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setServiceAccountId("113861329637993690965@sfbs-19116.iam.gserviceaccount.com")
                    .setDatabaseUrl("https://sfbs-19116-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .build();
            
                    if(FirebaseApp.getApps().isEmpty()){
                        FirebaseApp.initializeApp(options);
                    }
            System.out.println("\n\nFIREBASE CONNECTION ESTABLISHED!");
            // Count the total run of the application in Realtime Database
            runCount();

        } catch (Exception e) {
            System.out.println("\nFirebase Initialization Error: " + e.getMessage());
            e.printStackTrace();
        }

        //Initialize Firestore
        try(InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("ServiceAccountKey.json")) {  
            if (serviceAccount == null) {
                throw new IOException("ServiceAccountKey.json not found");
            }
            FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();
            firestore = firestoreOptions.getService();
            System.out.println("FIRESTORE CONNECTION ESTABLISHED!");
            // Count the total run of the application in Firestore
            runCountFirestore();
            
        } catch (Exception e) {
            System.out.println("\nFirestore Initialization Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        try (InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("ServiceAccountKey.json")) {
            if (serviceAccount == null) {
                throw new IOException("ServiceAccountKey.json not found");
            }
            storage = StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build()
                    .getService();

            System.out.println("FIREBASE STORAGE CONNECTION ESTABLISHED!\n\n");
        } catch (Exception e) {
            System.out.println("\nFirebase Storage Initialization Error: " + e.getMessage());
            e.printStackTrace();
        }

        
    }

    @Bean
    public FirebaseAuth firebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Bean
    public Firestore firestore() {
        return firestore;
    }

    @Bean
    public Storage storage() {
        return storage;
    }

    private void runCount() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference countRef = database.getReference("RunCount");

        countRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Integer count = dataSnapshot.getValue(Integer.class);
                    count++;
                    countRef.setValue(count, null);
                } else {
                    countRef.setValue(1, null);
                }

                // Add current timestamp with date and time
                DatabaseReference timestampRef = database.getReference("Timestamp");
                String currentTimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                timestampRef.setValue(currentTimestamp, null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Failed to read timestamp value: " + databaseError.getMessage());
            }
        });
    }

    private void runCountFirestore() {
        Firestore firestore = FirestoreClient.getFirestore();
        DocumentReference docRef = firestore.collection("RunCount").document("RunCount");
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document;
        try {
            document = future.get();
            if (document.exists()) {
                @SuppressWarnings("null")
                Integer count = document.getLong("count").intValue();
                count++;
                Map<String, Object> updates = new HashMap<>();
                updates.put("count", count);
                updates.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                docRef.update(updates);
            } else {
                Map<String, Object> data = new HashMap<>();
                data.put("count", 1);
                data.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                docRef.set(data);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
