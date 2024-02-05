package projet.ais.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import projet.ais.CodeGenerator;
import projet.ais.IdGenerator;

// import com.greenapi.MessageSender;

import projet.ais.config.GreenApiConfig;
import projet.ais.models.MessageWa;
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
    

    public MessageWa sendMessageAndSave(String whatsAppActeur, String msg) throws Exception {
        // Créer une instance de MessageWa et définir les valeurs
        MessageWa message = new MessageWa();
        message.setActeurConcerner("Ibrahim sy");
        message.setText(msg);
        message.setDateAjout(LocalDateTime.now());
        
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
}
