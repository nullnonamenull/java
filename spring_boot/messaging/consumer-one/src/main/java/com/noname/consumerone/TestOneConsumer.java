package com.noname.consumerone;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TestOneConsumer {

    private final List<PoJo> pojoList = new ArrayList<>();

    @RabbitListener(queues = ConsumerTopology.QUEUE)
    public void on(PoJo event) {
        pojoList.add(event);
    }

    public record PoJo(String a, int b) {
    }
}
