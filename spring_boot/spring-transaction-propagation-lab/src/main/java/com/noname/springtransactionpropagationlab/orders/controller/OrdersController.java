package com.noname.springtransactionpropagationlab.orders.controller;

import com.noname.springtransactionpropagationlab.orders.dto.OrderDTO;
import com.noname.springtransactionpropagationlab.orders.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrdersController {

    private final OrderService orderService;

    @PostMapping
    public void processOrder(@RequestBody OrderDTO orderDTO) {
        orderService.processOrder(orderDTO);
    }

}
