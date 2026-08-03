package com.noname.producer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EventService {

    private final OutboxRecorder outboxRecorder;

    public EventService(OutboxRecorder outboxRecorder) {
        this.outboxRecorder = outboxRecorder;
    }

    @Transactional
    public UUID emitTestOne() {
        return outboxRecorder.record("Test", "1", "test.one", new SomeDto("test", 123));
    }

}
