package com.noname.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
        var failed = new HashMap<UUID, String>();

        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        for (var kwitek : kwitki) {
            try {
                long left = deadline - System.nanoTime();
                var result = kwitek.getFuture().get(Math.max(0, left), TimeUnit.NANOSECONDS);

                if (kwitek.getReturned() != null) {
                    var returned = kwitek.getReturned();
                    logger.error("Outbox {} returned as unroutable: exchange={} routingKey={} replyCode={} replyText={}",
                            kwitek.getId(), returned.getExchange(), returned.getRoutingKey(),
                            returned.getReplyCode(), returned.getReplyText());
                    failed.put(UUID.fromString(kwitek.getId()), returned.getReplyText());
                } else if (!result.ack()) {
                    logger.warn("Outbox {} nacked by broker, reason={}", kwitek.getId(), result.reason());
                    failed.put(UUID.fromString(kwitek.getId()), Objects.toString(result.reason(), "No reason given"));
                } else {
                    ok.add(UUID.fromString(kwitek.getId()));
                }
            } catch (TimeoutException e) {
                logger.warn("Outbox {} not confirmed within the batch budget, left to the lease", kwitek.getId(), e);
            } catch (ExecutionException e) {
                logger.error("Outbox {} confirm future failed unexpectedly", kwitek.getId(), e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn("Relay interrupted while waiting for confirms, stopped after outbox {}", kwitek.getId(), e);
                break;
            }
        }

        if (!ok.isEmpty() || !failed.isEmpty()) {
            outboxPublisher.settle(ok, failed);
        }
    }
}
