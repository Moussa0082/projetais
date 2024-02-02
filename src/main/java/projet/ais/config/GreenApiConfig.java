package projet.ais.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class GreenApiConfig {
    
    @Value("${API_ID_INSTANCE}")
    private String apiId;

    @Value("${API_TOKEN_INSTANCE}")
    private String apiToken;

}
