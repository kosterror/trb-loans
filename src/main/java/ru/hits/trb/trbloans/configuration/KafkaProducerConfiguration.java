package ru.hits.trb.trbloans.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfiguration {

    private final KafkaTopics topics;

    @Bean
    public NewTopic loanPayment() {
        return TopicBuilder.name(topics.getLoanRepayment())
                .build();
    }

}
