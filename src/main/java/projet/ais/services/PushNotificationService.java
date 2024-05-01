// package projet.ais.services;

// import java.io.IOException;


// import org.springframework.core.io.ClassPathResource;
// import org.springframework.stereotype.Service;
// // import com.google.firebase.messaging.FirebaseMessagingException;
// // import com.google.firebase.messaging.Message;
// import org.yaml.snakeyaml.internal.Logger;

// import com.google.api.client.util.Value;
// import com.google.auth.oauth2.GoogleCredentials;
// import com.google.firebase.FirebaseApp;
// import com.google.firebase.FirebaseOptions;
// import com.google.firebase.messaging.AndroidConfig;
// import com.google.firebase.messaging.ApnsConfig;
// import com.google.firebase.messaging.FirebaseMessaging;
// import com.google.firebase.messaging.FirebaseMessagingException;
// import com.google.firebase.messaging.Message;
// import com.google.firebase.messaging.Notification;

// import jakarta.annotation.PostConstruct;

// @Service
// public class PushNotificationService {
    
//  // ... (initialisation de FCM Admin SDK)
//      @Value("${app.firebase-configuration-file}")
//     private String firebaseConfigPath;
//     Logger logger = LoggerFactory.getLogger(FCMInitializer.class);
//     @PostConstruct
//     public void initialize() {
//         try {
//             FirebaseOptions options = new FirebaseOptions.Builder()
//                     .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())).build();
//             if (FirebaseApp.getApps().isEmpty()) {
//                 FirebaseApp.initializeApp(options);
//                 logger.info("Firebase application initialized");
//             }
//         } catch (IOException e) {
//             logger.error(e.getMessage());
//         }
//     }

// //  public void sendPurchaseNotification(String userId, String productId) throws FirebaseMessagingException {
// //     Message message = Message.builder()
// //         .setToken(getUserFcmToken(userId)) // Remplacer par la récupération du token FCM de l'utilisateur
// //         .setNotification(new Notification("Achat de votre produit", "Le produit " + productId + " a été acheté"))
// //         .build();

// //     FirebaseMessaging.getInstance().send(message);
// //   }

//   // ... (méthode pour récupérer le token FCM de l'utilisateur)
// }
