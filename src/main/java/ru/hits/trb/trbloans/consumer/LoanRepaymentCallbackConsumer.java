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
import ru.hits.trb.trbloans.service.RepaymentService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanRepaymentCallbackConsumer {

    private static final String TOPIC = "${kafka.topic.loan-repayment-callback}";
    private static final String GROUP_ID = "${spring.kafka.consumer.group-id}";

    private final ObjectMapper mapper;
    private final RepaymentService repaymentService;

    @KafkaListener(topics = TOPIC, groupId = GROUP_ID)
    public void transactionLoanRepayment(@Header(KafkaHeaders.RECEIVED_KEY) String key,
                                         @Payload String message) {
        try {
            var transactionState = mapper.readValue(message, TransactionState.class);
            var loanRepaymentId = mapper.readValue(key, UUID.class);

            log.info("repayment id: {}, state: {}", loanRepaymentId, transactionState);
            repaymentService.processRepaymentCallback(loanRepaymentId, transactionState);
        } catch (JsonProcessingException exception) {
            throw new InternalServiceException("Failed to deserialize message: " + message, exception);
        }
    }

}
