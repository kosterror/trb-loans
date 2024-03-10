package ru.hits.trb.trbloans.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbloans.configuration.KafkaTopics;
import ru.hits.trb.trbloans.dto.UnidirectionalTransactionDto;
import ru.hits.trb.trbloans.exception.InternalServiceException;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanPaymentProducer {

    private final KafkaTemplate<Object, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final KafkaTopics topics;

    public void sendMessage(UUID loanId, UnidirectionalTransactionDto transactionDto) {
        try {
            var key = objectMapper.writeValueAsString(loanId);
            var value = objectMapper.writeValueAsString(transactionDto);

            kafkaTemplate.send(topics.getLoanPayment(), key, value);

            log.info("Payment record sent, id: {}, value: {}", key, value);
        } catch (JsonProcessingException exception) {
            throw new InternalServiceException("Failed to serialize data for kafka record", exception);
        }
    }

}
