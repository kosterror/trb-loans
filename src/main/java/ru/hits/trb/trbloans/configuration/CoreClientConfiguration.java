package ru.hits.trb.trbloans.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class CoreClientConfiguration {

    @Value("${urls.core}")
    private String url;

    @Bean
    public RestClient coreRestClient() {
        return RestClient.builder()
                .baseUrl(url)
                .build();
    }

}
