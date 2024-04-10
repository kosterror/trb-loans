package ru.hits.trb.trbloans.consumer;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbloans.dto.transaction.TransactionState;
import ru.hits.trb.trbloans.exception.InternalServiceException;
import ru.hits.trb.trbloans.service.LoanPaymentService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanPaymentCallbackConsumer {

    private static final String TOPIC = "${kafka.topic.consumer.loan-payment-callback}";

    private final ObjectMapper mapper;
    private final LoanPaymentService loanPaymentService;

    @KafkaListener(topics = TOPIC)
    public void transactionLoanRepayment(@Header(KafkaHeaders.RECEIVED_KEY) String key,
                                         @Payload String message) {
        try {
            var transactionState = mapper.readValue(message, TransactionState.class);
            var loanId = mapper.readValue(key, UUID.class);

            log.info("Loan id: {}, state: {}", loanId, transactionState);
            loanPaymentService.processPaymentCallback(loanId, transactionState);
        } catch (JsonProcessingException exception) {
            throw new InternalServiceException(STR."Failed to deserialize key '\{key}' or message '\{message}'");
        }
    }

}
