package com.noname.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;
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
        var ok = new ArrayList<UUID>();

        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        for (var kwitek : kwitki) {
            try {
                long left = deadline - System.nanoTime();
                var result = kwitek.getFuture().get(Math.max(0, left), TimeUnit.NANOSECONDS);

                if (result.ack() && kwitek.getReturned() == null) {
                    ok.add(UUID.fromString(kwitek.getId()));
                }
            } catch (Exception e) {
                logger.error("Confirm failed for outbox {}", kwitek.getId(), e);
            }
        }

        if (!ok.isEmpty()) {
            outboxPublisher.settle(ok);
        }
    }
}
