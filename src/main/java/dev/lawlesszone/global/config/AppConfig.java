package dev.lawlesszone.global.config;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    String apiKey = "8710522058100173";
    String secretKey = "Qpobt6L7MZz8PK3567wGExmjLZ0E87nmk6Z0ziuQzYmriKVI7ea9UhwaRkODYVaGA4MmHuLmmsXbyiQE";

    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, secretKey);
    }
}
