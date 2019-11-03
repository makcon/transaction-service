package test.maksim.transaction.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionServiceClientConfig {

    private final TransactionServiceClientFactory factory = new TransactionServiceClientFactory();

    @Bean
    public TransactionServiceClient contentClient(@Value("${transaction-service.base.url:http://localhost:8080}") String serviceUrl) {
        return factory.defaultClient(serviceUrl);
    }
}
