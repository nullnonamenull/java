package com.noname.springtransactionpropagationlab.orders.service;

import com.noname.springtransactionpropagationlab.auditLogs.service.AuditLogService;
import com.noname.springtransactionpropagationlab.orders.domain.Order;
import com.noname.springtransactionpropagationlab.orders.dto.OrderDTO;
import com.noname.springtransactionpropagationlab.orders.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final AuditLogService auditLogService;
    private final OrderRepository orderRepository;

    //    @Transactional // REQUIRED - default
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processOrder(final OrderDTO orderDTO) {
        var order = new Order();
        order.setId(UUID.randomUUID());
        order.setName(orderDTO.name());
        order.setStatus("CREATED");

        orderRepository.save(order);

        try {
            auditLogService.saveAudit(order.getId(), order.getName(), order.getStatus());
        } catch (RuntimeException e) {
            // ignored intentionally
        }

        if (true) {
            throw new RuntimeException();
        }
    }


}
