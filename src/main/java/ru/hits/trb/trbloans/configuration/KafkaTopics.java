package ru.hits.trb.trbloans.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties(prefix = "kafka.topic")
public class KafkaTopics {

    private String loanRepayment;

    private String loanPayment;

}
