package com.noname.producer;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitConfig {

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public TopicExchange appEventExchange() {
        return new TopicExchange(EventPublisher.EXCHANGE, true, false);
    }

    @Bean
    public Queue testOneQueue() {
        return QueueBuilder.durable("test.one.q")
                .quorum()
                // group size capped by cluster size — single node gives 1 replica
//                .withArgument("x-quorum-initial-group-size", 5) // 5 replicas: 1 leader + 4 followers
                .ttl(100) // x-message-ttl - in ms -> then dead letter
                .expires(200_00)
                .build();
    }

    @Bean
    public Binding testOneBinding(Queue testOneQueue, TopicExchange appEventsExchange) {
        return BindingBuilder.bind(testOneQueue).to(appEventsExchange).with("test.one");
    }

}
