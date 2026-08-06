package com.noname.producer;

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
import java.util.UUID;

@Component
public class OutboxPublisher {

    public static final String EXCHANGE = "app.events.v1";

    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;
    private final RabbitTemplate rabbitTemplate;


    public OutboxPublisher(ObjectMapper objectMapper, OutboxRepository outboxRepository, RabbitTemplate rabbitTemplate) {
        this.objectMapper = objectMapper;
        this.outboxRepository = outboxRepository;
        this.rabbitTemplate = rabbitTemplate;
    }


    @Transactional
    public List<CorrelationData> publishBatch(int limit) {
        var batch = outboxRepository.lockBatch(limit);
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

        return kwitki;
    }

    @Transactional
    public void settle(List<UUID> ok) {
        outboxRepository.deleteAllByIdInBatch(ok);
    }
}
