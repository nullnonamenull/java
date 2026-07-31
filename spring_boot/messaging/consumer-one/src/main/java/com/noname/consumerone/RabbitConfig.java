package com.noname.consumerone;

import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class RabbitConfig {

    static final String QUEUE = "test.one.q";

    @Bean
    MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }

}
