package ru.hits.trb.trbloans.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.hits.trb.trbloans.dto.transaction.InitTransactionDto;
import ru.hits.trb.trbloans.exception.InternalServiceException;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionProducer {

    private final KafkaTemplate<Object, Object> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.producer.transaction-initialization}")
    private String topic;

    public void sendMessage(UUID internalTransactionId, InitTransactionDto initTransaction) {
        try {
            var key = objectMapper.writeValueAsString(internalTransactionId);
            var value = objectMapper.writeValueAsString(initTransaction);

            kafkaTemplate.send(topic, key, value);

            log.info("Transaction initialization sent record, id: {}, message {}", key, value);
        } catch (JsonProcessingException exception) {
            throw new InternalServiceException("Failed to serialize data for kafka record", exception);
        }
    }


}
