package com.noname.producer;

import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EventPublisher {

    public static final String EXCHANGE = "app.events.v1";

    private final RabbitTemplate rabbitTemplate;

    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(cron = "0 * * * * *")
    public void publishTestOne() {
        send(new SomeDto("Test", 123));
    }

    private void send(Object payload) {
        final var  messageId = UUID.randomUUID().toString();
        rabbitTemplate.convertAndSend(EXCHANGE, "test.one", payload, message -> {
            var props = message.getMessageProperties();
            props.setMessageId(messageId);
            props.setContentType("application/json");
            props.setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        }, new CorrelationData(messageId));
    }

    public record SomeDto(String a, int b) {
    }
}
