package com.fyp.sfbs_fyp;
import java.io.FileInputStream;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Service
public class FirebaseInitialization {

    public void Initialization(){
        FileInputStream serviceAccount = null;
        
        try {
            serviceAccount = new FileInputStream("./ServiceAccountKey.json");
            FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setDatabaseUrl("https://sfbs-19116-default-rtdb.asia-southeast1.firebasedatabase.app")
            .build();
    
            FirebaseApp.initializeApp(options);
            System.out.println("/n/n DATABASE CONNECTION ESTABLISHED! /n/n");


        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println("/n/n DATABASE WONT RECONNECT /n/n");
            e.printStackTrace();
        }

       
            }
    
}
