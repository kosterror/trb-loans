package ru.hits.trb.trbloans.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hits.trb.trbloans.dto.loanapplication.LoanRepaymentTransaction;
import ru.hits.trb.trbloans.producer.LoanRepaymentProducer;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final LoanRepaymentProducer producer;

    @PostMapping("/test")
    public void test() {
        producer.sendMessage(
                UUID.randomUUID(),
                LoanRepaymentTransaction.builder()
                        .accountId(UUID.randomUUID())
                        .amount(1000)
                        .build()
        );
    }

}
