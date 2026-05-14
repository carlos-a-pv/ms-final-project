package co.edu.uniquindio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient departmentWebClient(){
        return WebClient.builder()
                .baseUrl("http://ms-departments:8081")
                .build();
    }
}
