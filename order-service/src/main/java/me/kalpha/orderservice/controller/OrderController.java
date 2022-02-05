package me.kalpha.orderservice.controller;

import me.kalpha.orderservice.dto.OrderDto;
import me.kalpha.orderservice.jpa.OrderEntity;
import me.kalpha.orderservice.service.OrderService;
import me.kalpha.orderservice.vo.RequestOrder;
import me.kalpha.orderservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order-service")
public class OrderController {
    private ModelMapper mapper;
    private OrderService orderService;

    @Autowired
    public OrderController(ModelMapper mapper, OrderService orderService) {
        this.mapper = mapper;
        this.orderService = orderService;
    }

    @GetMapping("/health_check")
    public String status(HttpServletRequest request) {
        return String.format("It's working in Order Service on PORT : %s!", request.getServerPort());
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder (@PathVariable("userId") String userId,
                                                      @RequestBody RequestOrder requestOrder) {
        OrderDto orderDto = mapper.map(requestOrder, OrderDto.class);
        orderDto.setUserId(userId);
        OrderDto createDto = orderService.createOrder(orderDto);
        ResponseOrder responseOrder = mapper.map(createDto, ResponseOrder.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrders(@PathVariable("userId") String userId) {
        Iterable<OrderEntity> orderEntities = orderService.getOrdersByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();
        orderEntities.forEach(v -> {
            result.add(mapper.map(v, ResponseOrder.class));
        });

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public ResponseEntity<ResponseOrder> getOrder(@PathVariable("userId") String userId,
                                                  @PathVariable("orderId") String orderId) {
        OrderDto orderDto = orderService.getOrderByOrderId(orderId);

        ResponseOrder result = mapper.map(orderDto, ResponseOrder.class);
        return ResponseEntity.ok(result);
    }
}
