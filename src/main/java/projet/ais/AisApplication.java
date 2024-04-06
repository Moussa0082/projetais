package projet.ais;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.awt.image.BufferedImage;
// import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

@SpringBootApplication
public class AisApplication {
	
	

	public static void main(String[] args) {
		SpringApplication.run(AisApplication.class, args);
	}
	

	//  @Bean
    // public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
    //  return new BufferedImageHttpMessageConverter();
    // }

	
}


