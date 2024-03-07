package ru.hits.trb.trbloans.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbloans.configuration.KafkaTopics;
import ru.hits.trb.trbloans.controller.exception.InternalServiceException;
import ru.hits.trb.trbloans.dto.loanapplication.LoanRepaymentTransaction;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanRepaymentProducer {

    private final KafkaTemplate<Object, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final KafkaTopics topics;

    public void sendMessage(UUID loanPaymentId, LoanRepaymentTransaction loanRepaymentTransaction) {
        try {
            var key = objectMapper.writeValueAsString(loanPaymentId);
            var value = objectMapper.writeValueAsString(loanRepaymentTransaction);

            kafkaTemplate.send(topics.getLoanRepayment(), key, value);

            log.info("message sent, id: {}, body {}", loanPaymentId, loanRepaymentTransaction);
        } catch (JsonProcessingException exception) {
            throw new InternalServiceException("Failed to serialize data for kafka record", exception);
        }
    }

}
