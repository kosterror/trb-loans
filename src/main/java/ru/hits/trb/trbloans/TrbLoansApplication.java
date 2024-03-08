package ru.hits.trb.trbloans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.hits.trb.trbloans.configuration.KafkaTopics;

@SpringBootApplication
@EnableConfigurationProperties(KafkaTopics.class)
public class TrbLoansApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrbLoansApplication.class, args);
	}

}
