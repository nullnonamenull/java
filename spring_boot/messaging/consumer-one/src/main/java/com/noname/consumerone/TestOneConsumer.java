package com.noname.consumerone;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TestOneConsumer {

    private final TestInMemoryService testInMemoryService;

    public TestOneConsumer(TestInMemoryService testInMemoryService) {
        this.testInMemoryService = testInMemoryService;
    }

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void on(SomeDto event) throws Exception {
//        if (true ){
//            throw new Exception();
//        }

        testInMemoryService.create(event);
    }
}
