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
public class LoanRepaymentProducer {

    private final KafkaTemplate<Object, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final KafkaTopics topics;

    public void sendMessage(UUID loanRepaymentId, UnidirectionalTransactionDto transactionDto) {
        try {
            var key = objectMapper.writeValueAsString(loanRepaymentId);
            var value = objectMapper.writeValueAsString(transactionDto);

            kafkaTemplate.send(topics.getLoanRepayment(), key, value);

            log.info("Repayment record sent, id: {}, value {}", key, value);
        } catch (JsonProcessingException exception) {
            throw new InternalServiceException("Failed to serialize data for kafka record", exception);
        }
    }

}
