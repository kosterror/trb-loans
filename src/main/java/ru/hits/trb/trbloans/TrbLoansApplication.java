package ru.hits.trb.trbloans;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TrbLoansApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrbLoansApplication.class, args);
    }

}
