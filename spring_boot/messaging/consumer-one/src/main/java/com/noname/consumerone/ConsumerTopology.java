package com.noname.consumerone;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration(proxyBeanMethods = false)
public class ConsumerTopology {

    static final String QUEUE = "test.one.q";

//    @Bean
//    TopicExchange appEvents() {
//        return ExchangeBuilder.topicExchange("app.events.v1").durable(true).build();
//    }

//    @Bean
//    Queue testOneQueue() {
//        return QueueBuilder.durable(QUEUE)
//                .quorum()
//                .deliveryLimit(15)
//                .build();
//    }

    @Bean
    MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

//    @Bean
//    Binding testOneBinding(Queue testOneQueue, TopicExchange appEvents) {
//        return BindingBuilder.bind(testOneQueue).to(appEvents).with("test.one");
//    }

}
