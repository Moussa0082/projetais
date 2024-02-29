package projet.ais.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;

// import com.greenapi.MessageSender;

import projet.ais.config.GreenApiConfig;
import projet.ais.models.Acteur;
import projet.ais.models.CategorieProduit;
import projet.ais.models.MessageWa;
import projet.ais.models.Niveau1Pays;
import projet.ais.repository.ActeurRepository;
import projet.ais.repository.MessageWaRepository;

@Service
public class MessageService {
    
    @Autowired
    GreenApiConfig greenApiConfig;
    @Autowired
    MessageWaRepository messageRepository;
    @Autowired
    CodeGenerator codeGenerator;
    @Autowired
    IdGenerator idGenerator;
    @Autowired
    SendMessage sendMessage;
  


    // public MessageWa sendMessageAndSave(String whatsAppActeur, String msg, String acteur) throws Exception {
    //     // Créer une instance de MessageWa et définir les valeurs
    //     MessageWa message = new MessageWa();
    //     // message.setActeurConcerner(acteur);
    //     message.setText(msg);
    //     String pattern = "yyyy-MM-dd HH:mm";
    //     DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    //     LocalDateTime now = LocalDateTime.now();
    //     String formattedDateTime = now.format(formatter);  
    //     message.setDateAjout(formattedDateTime);
        
    //     // Générer le code et l'ID
    //     String codes = codeGenerator.genererCode();
    //     String idCode = idGenerator.genererCode();
    //     message.setCodeMessage(codes);
    //     message.setIdMessage(idCode);
        
    //     // Enregistrer le message dans la base de données
    //     messageRepository.save(message);
        
    //     // Envoyer le message
    //     sendMessage.sendMessages(whatsAppActeur, msg);
        
    //     return message;
    // }

    public MessageWa sendMessageAndSave(String whatsAppActeur, String msg, Acteur acteur) throws Exception {
        // Créer une instance de MessageWa et définir les valeurs
        MessageWa message = new MessageWa();
        message.setActeur(acteur);
        message.setText(msg);
        String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);
        message.setDateAjout(formattedDateTime);
        
        // Générer le code et l'ID
        String codes = codeGenerator.genererCode();
        String idCode = idGenerator.genererCode();
        message.setCodeMessage(codes);
        message.setIdMessage(idCode);
        
        // Enregistrer le message dans la base de données
        messageRepository.save(message);
        
        // Envoyer le message
        sendMessage.sendMessages(whatsAppActeur, msg);
        
        return message;
    }
    public MessageWa sendMessagePersonnalAndSave(String whatsAppActeur, String msg) throws Exception {
        // Créer une instance de MessageWa et définir les valeurs
        MessageWa message = new MessageWa();
        // message.setActeurConcerner("Admin");
        message.setText(msg);
         String pattern = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime now = LocalDateTime.now();
        String formattedDateTime = now.format(formatter);  
        message.setDateAjout(formattedDateTime);
        
        // Générer le code et l'ID
        String codes = codeGenerator.genererCode();
        String idCode = idGenerator.genererCode();
        message.setCodeMessage(codes);
        message.setIdMessage(idCode);
        
        // Enregistrer le message dans la base de données
        messageRepository.save(message);
        
        // Envoyer le message
        sendMessage.sendMessages(whatsAppActeur, msg);
        
        return message;
    }

    public List<MessageWa> getAllMessage(){
        List<MessageWa> messageList  = messageRepository.findAll();

        if(messageList.isEmpty())
            throw new EntityNotFoundException("Aucun message trouvé");

            messageList.sort(Comparator.comparing(MessageWa::getText));
        return messageList;
    }

    public List<MessageWa> getAllMessageByActeur(String id){
        List<MessageWa> messageList  = messageRepository.findByActeurIdActeur(id);

        if(messageList.isEmpty())
            throw new EntityNotFoundException("Aucun message trouvé");

            messageList.sort(Comparator.comparing(MessageWa::getText));
        return messageList;
    }

    @Transactional
    public String deleteAllMessages() {
        messageRepository.deleteAll();
        return "Tous les messages supprimé avec succèss";
    }

    public String deleteMessage(String id, String idActeur) {
        // Trouver le message par son ID
        MessageWa messageWa = messageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Message non trouvé"));

        // Vérifier si l'acteur concerné correspond
        if (!messageWa.getActeur().getIdActeur().equals(idActeur)) {
            throw new IllegalArgumentException("Vous n'êtes autorisé à supprimer ce message");
        }

        // Supprimer le message
        messageRepository.delete(messageWa);

        return "Message supprimé avec succès";
    }
}
