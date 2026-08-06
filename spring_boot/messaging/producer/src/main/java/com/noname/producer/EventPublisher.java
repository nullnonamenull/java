package com.noname.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@Component
public class EventPublisher {

    public static final String EXCHANGE = "app.events.v1";

    private static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public EventPublisher(RabbitTemplate rabbitTemplate, OutboxRepository outboxRepository, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 1000)
    @Transactional
    public void relay() {
        var batch = outboxRepository.lockBatch(100);
        var kwitki = new ArrayList<CorrelationData>();

        for (var row : batch) {
            byte[] body = objectMapper.writeValueAsBytes(row.getPayload());
            var props = MessagePropertiesBuilder.newInstance()
                    .setMessageId(row.getId().toString())
                    .setContentType("application/json")
                    .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                    .build();

            var kwitek = new CorrelationData(row.getId().toString());

            rabbitTemplate.send(EXCHANGE, row.getEventType(), new Message(body, props), kwitek);
            kwitki.add(kwitek);

            row.setAttempts(row.getAttempts() + 1);
            row.setNextAttemptAt(OffsetDateTime.now().plusSeconds(30));
        }

        for (var kwitek : kwitki) {
            try {
                var result = kwitek.getFuture().get(5, TimeUnit.SECONDS);
                System.out.println(kwitek.getId() + " ack=" + result.ack());
            } catch (Exception e) {
                logger.error("Confirm failed for outbox {}", kwitek.getId(), e);
            }
        }
    }
}
