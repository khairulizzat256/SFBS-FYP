package com.fyp.sfbs_fyp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Configuration
public class FirebaseInitialization {

    private boolean isInitialized;

    @Bean
    public FirebaseAuth firebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @PostConstruct
    public void Initialization() {
        if (isInitialized) {
            System.out.println("\n\nDatabase still in connection\n\n");
            return;
        }
        try {
            String serviceAccountKeyJson = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"sfbs-19116\",\n" +
                    "  \"private_key_id\": \"9951f9650e2becb866fc64e57d91113802e8ef54\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDdZ+l94WX/f9FH\\nhuz3ysbiAXjxa78yWlVz+oqYh3NeFT7NqEg3XsmcADLJrpBDJi2oKw/M3rfCGauN\\nQiy/zLTFcDqP04LER6tddiO4SpjyCvhdx8GL4AWiWAQELGfuu8nko+smMp1KqtNA\\nVEnQ5tRat4wie2YWp7qqnxez+iiuaWtAfSBR0veQmKCma7fxiit0gGjIuvdIg/b0\\nVE+CPmTdo/jm+zquPzdf6sa86CD3yWnLcaujbvDaWOmJQ7G/iPLqCrUFVuVkX6a2\\nAomnX86AAssp/5wJj0+GMR+kRHaSkGXEUJlfvNCIXnnAmBz5tTo3SbhtgoPTrvzv\\nL8PlVhWFAgMBAAECggEACpJ8wwgeuCZTk+GKEmQCVAZDeZhjncUpVEYtVOQPkCQe\\nEVvHhQNXnQV3vLXYntLldmSwrApM29h2Hrg/TZz6BdKFLVspdkjA0qVCm+POAq10\\n7M940GkkWRCbB9L8rIOW5qjQIKJlQeHp1VDLtzgKgawcOtpJxpZVnXJG2LnUNE3E\\nH96AOzb2vGub7I/xvhX8KXFgtdWe2Wj8xLMGzp/mBGBDyft2Vmkej3qlzj/faP1C\\nJxTAC/tnbThkBBjkAJJI9w7+/nyxVAd7UNohJbZjgkKPUFbZ3m78/2hm4cteSx+t\\nXr14WkFtg2e0iblXsR0IcXqEBloldAqe8cLP8Jc6AQKBgQDztD9Tu1gmrdXlWFWB\\nGSijDYtks9bzgNwstvatoMOHIqGNUaT6nm6tuAHh+y83+FtUT5VxOzcGssY1QE/e\\nY3/I5nFhZq4eLEfAbPdjEOHAo77X+oyZjFmydMNI7aXKCeKQqTfNCv6q/fSkBGbw\\nsjeclI6ISCushhp4mldAjfUPhQKBgQDok6enE5VmTB3vkiGrT8925uVZ/HHsbBh5\\nfYYjfMoIzGrkMoRh2AARhKKQ9OBY5aJb478Ddpl8zJngexQyyZUbpJYZDldQ7qmG\\nJske+ralCVBGolK9xwP2wu6taBLuZpUnwWeU85H/fPBP9OfqziZKsSAUcgfMwV//\\n6daYPJTOAQKBgFehwvvK7CHMdJOpteVcNJ6dKjil6m9ZMNLrX7yDiIu5SjpU12w/\\nODFDb3nhJtOLfNvOWkCDOIAyHM/dNlsTkEmfg7mwgH/dEFgDVtnKokLxHpjvQUud\\nikt/bRF2Ux1ZfJzkHGqgv36hHy0zYAjBLPoi+TXMLdg1yKMBbMqLwUk5AoGBAM8T\\nQG9q6VdGqFS46I9uJQr5s/U15n0ZFYV+NKN+JShGUVnSfLndJtpyHIgx71pXQwLL\\nuTOtMiXCybLsAuThRIhzN00hzq55gKi3+dRCZ8Jzk20Ac+kSvD3F+9qbUle/MEhQ\\nVBhz8Wbn59NY+/C9EazpHeX2TWmKNx5VJkMvlVIBAoGAJJT8/945ix1XyU9CShlf\\nbVnDRUReKAvPfJWcF2Sj3k7xPVvKQ/DmGToI6pnbVhka9UH0v7ZgYzqdSZHhXyzD\\nhp9a3SFewRFUwHX8800N6UZONFSkJdwXtHJHdkyIS4KiKY5xgQA2+bkLKdScsQHm\\npmjOVdZTbCoU21igVWR9+FQ=\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-8a0e4@sfbs-19116.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"113861329637993690965\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-8a0e4%40sfbs-19116.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}";

            InputStream serviceAccount = new ByteArrayInputStream(serviceAccountKeyJson.getBytes());
            @SuppressWarnings("deprecation")
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setServiceAccountId("113861329637993690965@sfbs-19116.iam.gserviceaccount.com")
                    .setDatabaseUrl("https://sfbs-19116-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("\n\nDATABASE CONNECTION ESTABLISHED!\n\n");
            isInitialized = true;
            
                        //ESTABLISH COUNT IN DATABASE
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

        } catch (Exception e) {
            System.out.println("\n\nDATABASE COULD NOT BE CONNECTED");
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
