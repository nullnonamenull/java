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

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class OutboxPublisher {

    public static final String EXCHANGE = "app.events.v1";
    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);

    private final OutboxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;


    public OutboxPublisher(OutboxRepository outboxRepository, RabbitTemplate rabbitTemplate) {
        this.outboxRepository = outboxRepository;
        this.rabbitTemplate = rabbitTemplate;
    }


    public List<CorrelationData> publish(List<OutboxRow> rows) {
        var kwitki = new ArrayList<CorrelationData>();

        for (var row : rows) {
            try {
                var props = MessagePropertiesBuilder.newInstance()
                        .setMessageId(row.id().toString())
                        .setContentType("application/json")
                        .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                        .build();

                var kwitek = new CorrelationData(row.id().toString());

                rabbitTemplate.send(EXCHANGE, row.eventType(), new Message(row.body(), props), kwitek);
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
            var actualAttempts = row.getAttempts() + 1;

            row.setAttempts(actualAttempts);
            row.setNextAttemptAt(OffsetDateTime.now().plusSeconds(backoffAttempts(actualAttempts)));

            rows.add(new OutboxRow(row.getId(), row.getEventType(), row.getPayload().getBytes(StandardCharsets.UTF_8)));
        }

        return rows;
    }

    private long backoffAttempts(int actualAttempts) {
        int exponent = Math.min(actualAttempts - 1, 20);
        long delay = Math.min(30L << exponent, 300L);
        return delay + ThreadLocalRandom.current().nextLong(delay / 5);
    }

    @Transactional
    public void settle(List<UUID> ok, Map<UUID, String> failed, List<UUID> poisoned) {
        outboxRepository.deleteAllByIdInBatch(ok);

        for (var row : outboxRepository.findAllById(failed.keySet())) {
            row.setLastError(failed.get(row.getId()));

            if (poisoned.contains(row.getId())) {
                row.setStatus("FAILED");
            } else {
                row.setFailures(row.getFailures() + 1);

                if (row.getFailures() >= 10) {
                    row.setStatus("FAILED");
                    log.error("Outbox {} parked as FAILED after {} failures, lastError={}", row.getId(), row.getFailures(), row.getLastError());
                }
            }
        }
    }
}
