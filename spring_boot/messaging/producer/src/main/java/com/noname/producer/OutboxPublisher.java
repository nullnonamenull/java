package com.noname.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class OutboxPublisher {

    public static final String EXCHANGE = "app.events.v1";
    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);

    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;


    public OutboxPublisher(ObjectMapper objectMapper, OutboxRepository outboxRepository, RabbitTemplate rabbitTemplate) {
        this.objectMapper = objectMapper;
        this.outboxRepository = outboxRepository;
        this.rabbitTemplate = rabbitTemplate;
    }


    public List<CorrelationData> publish(List<OutboxRow> rows) {
        var kwitki = new ArrayList<CorrelationData>();

        for (var row : rows) {
            try {
                byte[] body = objectMapper.writeValueAsBytes(row.payload());
                var props = MessagePropertiesBuilder.newInstance()
                        .setMessageId(row.id().toString())
                        .setContentType("application/json")
                        .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                        .build();

                var kwitek = new CorrelationData(row.id().toString());

                rabbitTemplate.send(EXCHANGE, row.eventType(), new Message(body, props), kwitek);
                kwitki.add(kwitek);
            } catch (AmqpException e) {
                log.warn("Broker unreachable, sent {} of {} rows, rest left to the lease", kwitki.size(), rows.size(), e);
                break;
            }

        }

        return kwitki;
    }

    @Transactional
    public List<OutboxRow> leaseBatch(int limit) {
        var batch = outboxRepository.lockBatch(limit);
        var rows = new ArrayList<OutboxRow>();

        for (var row : batch) {
            row.setAttempts(row.getAttempts() + 1);
            row.setNextAttemptAt(OffsetDateTime.now().plusSeconds(30));

            rows.add(new OutboxRow(row.getId(), row.getEventType(), row.getPayload()));
        }

        return rows;
    }

    @Transactional
    public void settle(List<UUID> ok, Map<UUID, String> failed) {
        outboxRepository.deleteAllByIdInBatch(ok);

        for (var row : outboxRepository.findAllById(failed.keySet())) {
            row.setLastError(failed.get(row.getId()));
        }
    }
}
