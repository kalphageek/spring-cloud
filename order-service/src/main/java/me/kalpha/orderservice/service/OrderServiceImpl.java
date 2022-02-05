package me.kalpha.orderservice.service;

import lombok.extern.slf4j.Slf4j;
import me.kalpha.orderservice.dto.OrderDto;
import me.kalpha.orderservice.jpa.OrderEntity;
import me.kalpha.orderservice.jpa.OrderRepository;
import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    private Environment env;
    private OrderRepository orderRepository;
    private ModelMapper mapper;

    @Autowired
    public OrderServiceImpl(Environment env, OrderRepository orderRepository, ModelMapper mapper) {
        this.env = env;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalAmount(orderDto.getQty()*orderDto.getUnitPrice());
        OrderEntity entity = mapper.map(orderDto, OrderEntity.class);
        orderRepository.save(entity);

        OrderDto returnDto = mapper.map(entity, OrderDto.class);
        return returnDto;
    }

    @Override
    public OrderDto getOrderByOrderId(String orderId) {
        OrderEntity entity = orderRepository.findByOrderId(orderId);
        return mapper.map(entity, OrderDto.class);
    }

    @Override
    public Iterable<OrderEntity> getOrdersByUserId(String userId) {
        return orderRepository.findByUserId(userId);
    }
}
