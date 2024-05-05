package ru.hits.trb.trbloans.configuration;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.observation.DefaultClientRequestObservationConvention;
import org.springframework.web.client.RestClient;

@Configuration
public class UsersClientConfiguration {

    @Value("${urls.users}")
    private String url;

    @Bean
    public RestClient usersRestClient(ObservationRegistry observationRegistry) {
        return RestClient.builder()
                .baseUrl(url)
                .observationRegistry(observationRegistry)
                .observationConvention(new DefaultClientRequestObservationConvention())
                .build();
    }

}
