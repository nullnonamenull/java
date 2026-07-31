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
        return QueueBuilder.durable("test.one.q").build();
    }

    @Bean
    public Binding testOneBinding(Queue testOneQueue, TopicExchange appEventsExchange) {
        return BindingBuilder.bind(testOneQueue).to(appEventsExchange).with("test.one");
    }

}
