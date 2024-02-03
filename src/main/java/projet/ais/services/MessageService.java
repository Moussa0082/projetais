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
    

    // https://api.greenapi.com/waInstance{{idInstance}}/sendMessage/{{apiTokenInstance}}
    public MessageWa sendMessage(MessageWa message){
    
        String codes = codeGenerator.genererCode();
        String idCode = idGenerator.genererCode();

        message.setCodeMessage(codes);
        message.setIdMessage(idCode);
        message.setDateAjout(LocalDateTime.now());

        return message;
    }
}
