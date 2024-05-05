package ru.hits.trb.trbloans.configuration;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.observation.DefaultClientRequestObservationConvention;
import org.springframework.web.client.RestClient;

@Configuration
public class CoreClientConfiguration {

    @Value("${urls.core}")
    private String url;

    @Bean
    public RestClient coreRestClient(ObservationRegistry observationRegistry) {
        return RestClient.builder()
                .baseUrl(url)
                .observationRegistry(observationRegistry)
                .observationConvention(new DefaultClientRequestObservationConvention())
                .build();
    }

}
