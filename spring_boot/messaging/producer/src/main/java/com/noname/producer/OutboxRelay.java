package com.noname.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class OutboxRelay {

    private static final Logger logger = LoggerFactory.getLogger(OutboxRelay.class);

    private final OutboxPublisher outboxPublisher;

    public OutboxRelay(OutboxPublisher outboxPublisher) {
        this.outboxPublisher = outboxPublisher;
    }

    @Scheduled(fixedDelay = 1000)
    public void relay() {
        var kwitki = outboxPublisher.publishBatch(100);

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
