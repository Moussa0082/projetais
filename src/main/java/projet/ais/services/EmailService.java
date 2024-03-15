package projet.ais.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import projet.ais.models.Alerte;
import projet.ais.repository.AlerteRepository;



@Service
public class EmailService {

     @Autowired
     AlerteRepository alerteRepository;

     @Autowired private JavaMailSender javaMailSender;
 
    @Value("bane8251@gmail.com") private String sender;
  

    public String sendSimpleMail(Alerte alerte) {
       // Method 1
    // To send a simple email
    
 
        // Try block to check for exceptions
        try {
 
            // Creating a simple mail message
            SimpleMailMessage mailMessage
                = new SimpleMailMessage();
 
            // Setting up necessary details
            mailMessage.setFrom(sender);
            mailMessage.setTo(alerte.getEmail());
            mailMessage.setText(alerte.getMessage());
            mailMessage.setSubject(alerte.getSujet());
 
            // Sending the mail
            javaMailSender.send(mailMessage);
            return "Email envoyer avec succès...";
        }
 
        // Catch block to handle the exceptions
        catch (Exception e) {
            return "Erreur lors de l'envoi de l'email ";
        }
    }   
    
    
}
