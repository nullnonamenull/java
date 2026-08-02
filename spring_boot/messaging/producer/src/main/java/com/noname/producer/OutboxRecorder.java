package com.noname.producer;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Component
public class OutboxRecorder {

    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;

    public OutboxRecorder(ObjectMapper objectMapper, OutboxRepository outboxRepository) {
        this.objectMapper = objectMapper;
        this.outboxRepository = outboxRepository;
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public UUID record(String aggregateType, String aggregateId, String eventType, Object payload) {
        var id = UUID.randomUUID();
        Map<String, Object> body = objectMapper.convertValue(payload, new TypeReference<>() {});

        var outboxMessage = new OutboxMessage();
        outboxMessage.setId(id);
        outboxMessage.setAggregateId(aggregateId);
        outboxMessage.setAggregateType(aggregateType);
        outboxMessage.setEventType(eventType);
        outboxMessage.setPayload(body);
        outboxMessage.setStatus("NEW");
        outboxMessage.setAttempts(0);
        outboxMessage.setHeaders(Map.of());
        outboxMessage.setCreatedAt(OffsetDateTime.now());
        outboxMessage.setNextAttemptAt(OffsetDateTime.now());
        outboxRepository.save(outboxMessage);

        return id;
    }

}
