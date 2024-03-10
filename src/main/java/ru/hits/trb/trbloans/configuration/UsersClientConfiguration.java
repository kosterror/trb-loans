package ru.hits.trb.trbloans.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class UsersClientConfiguration {

    @Value("${urls.users}")
    private String url;

    @Bean
    public RestClient usersRestClient() {
        return RestClient.builder()
                .baseUrl(url)
                .build();
    }

}
