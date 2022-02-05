package me.kalpha.orderservice.service;

import me.kalpha.orderservice.dto.OrderDto;
import me.kalpha.orderservice.jpa.OrderEntity;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto getOrderByOrderId(String orderId);
    Iterable<OrderEntity> getOrdersByUserId(String userId);
}
