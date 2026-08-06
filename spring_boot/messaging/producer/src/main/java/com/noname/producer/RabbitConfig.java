package com.noname.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.boot.amqp.autoconfigure.RabbitTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import static com.noname.producer.OutboxPublisher.EXCHANGE;

@Configuration
public class RabbitConfig {

    public static final String DLX = "test.dlx";

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    // MAIN
    @Bean
    public TopicExchange appEventExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Binding testOneBinding(Queue testOneQueue, TopicExchange appEventsExchange) {
        return BindingBuilder.bind(testOneQueue).to(appEventsExchange).with("test.one");
    }

    // DEAD LETTER
    @Bean
    public DirectExchange deadLetterExchange() {
        return ExchangeBuilder.directExchange(DLX).durable(true).internal().build();
    }

    @Bean
    public Queue testOneDlq() {
        return QueueBuilder.durable("test.one.dlq")
                .maxLength(50_000)
                .withArgument("x-message-ttl", 14L * 24 * 60 * 60 * 1000) // 14 days retention
                .quorum()
                .build();
    }

    @Bean
    public Binding testOneDlqBinding(Queue testOneDlq, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(testOneDlq).to(deadLetterExchange).with("test.one");
    }

    // QUEUE
    @Bean
    public Queue testOneQueue() {
        return QueueBuilder.durable("test.one.q")
                .quorum()
                .deliveryLimit(5)
                // group size capped by cluster size — single node gives 1 replica
//                .withArgument("x-quorum-initial-group-size", 5) // 5 replicas: 1 leader + 4 followers
                .maxLength(200_000)
                .overflow(QueueBuilder.Overflow.rejectPublish)
                .deadLetterExchange(DLX)
                .deadLetterRoutingKey("test.one")
                .build();
    }

    @Bean
    public RabbitTemplateCustomizer publishConfirms() {
        Logger log = LoggerFactory.getLogger(RabbitConfig.class);
        return template -> {
            template.setConfirmCallback((correlation, ack, cause) -> {
                String id = correlation != null ? correlation.getId() : "unknown";
                if (!ack) {
                    log.error("Broker odrzucił wiadomość id={}, powód={}", id, cause);
                }
            });
            template.setReturnsCallback(returned ->
                    log.error("Wiadomość nie trafiła do żadnej kolejki: exchange={}, rk={}, powód={}",
                            returned.getExchange(),
                            returned.getRoutingKey(),
                            returned.getReplyText()));
        };
    }

}
