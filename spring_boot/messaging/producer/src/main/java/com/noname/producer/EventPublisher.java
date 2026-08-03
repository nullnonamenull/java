package com.noname.producer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

@Component
public class EventPublisher {

    public static final String EXCHANGE = "app.events.v1";

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
        for (var row : batch) {
            byte[] body = objectMapper.writeValueAsBytes(row.getPayload());
            var props = MessagePropertiesBuilder.newInstance()
                    .setMessageId(row.getId().toString())
                    .setContentType("application/json")
                    .setDeliveryMode(MessageDeliveryMode.PERSISTENT)
                    .build();
            rabbitTemplate.send(EXCHANGE, row.getEventType(), new Message(body, props));
            outboxRepository.delete(row);
        }
    }
}
