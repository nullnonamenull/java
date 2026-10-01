package com.noname.springtransactionpropagationlab.orders.repository;

import com.noname.springtransactionpropagationlab.orders.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
