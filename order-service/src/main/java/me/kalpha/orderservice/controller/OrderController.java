package me.kalpha.orderservice.controller;

import lombok.extern.slf4j.Slf4j;
import me.kalpha.orderservice.dto.OrderDto;
import me.kalpha.orderservice.jpa.OrderEntity;
import me.kalpha.orderservice.messagequeue.KafkaProducer;
import me.kalpha.orderservice.messagequeue.OrderProducer;
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

@Slf4j
@RestController
@RequestMapping("/order-service")
public class OrderController {
    private ModelMapper mapper;
    private OrderService orderService;
    private KafkaProducer kafkaProducer;
    private OrderProducer orderProducer;

    @Autowired
    public OrderController(ModelMapper mapper, OrderService orderService, KafkaProducer kafkaProducer, OrderProducer orderProducer) {
        this.mapper = mapper;
        this.orderService = orderService;
        this.kafkaProducer = kafkaProducer;
        this.orderProducer = orderProducer;
    }

    @GetMapping("/health_check")
    public String status(HttpServletRequest request) {
        return String.format("It's working in Order Service on PORT : %s!", request.getServerPort());
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder (@PathVariable("userId") String userId,
                                                      @RequestBody RequestOrder requestOrder) {

        log.info("Before call order-service microservice (createOrder)");
        OrderDto orderDto = mapper.map(requestOrder, OrderDto.class);
        orderDto.setUserId(userId);

        /* Use DB + kafka */
//        OrderDto createDto = orderService.createOrder(orderDto);
//        ResponseOrder responseOrder = mapper.map(createDto, ResponseOrder.class);
//        kafkaProducer.send("example-order-topic", orderDto);

        /* Use kafka connect*/
        orderProducer.send("orders", orderDto);
        ResponseOrder responseOrder = mapper.map(orderDto, ResponseOrder.class);
        log.info("After call order-service microservice (createOrder)");

        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrders(@PathVariable("userId") String userId) throws Exception {

        log.info("Before call order-service  microservice (getOrders)");
        Iterable<OrderEntity> orderEntities = orderService.getOrdersByUserId(userId);

        List<ResponseOrder> result = new ArrayList<>();
        orderEntities.forEach(v -> {
            result.add(mapper.map(v, ResponseOrder.class));
        });

        // Error 발생하는 경우 sleuth 테스트
        try {
            Thread.sleep(1000);
            throw new Exception("장애발생");
        } catch (InterruptedException e) {
            log.warn(e.getMessage());
        }

        log.info("After call order-service microservice (getOrders)");

        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/{userId}/orders/{orderId}")
    public ResponseEntity<ResponseOrder> getOrder(@PathVariable("userId") String userId,
                                                  @PathVariable("orderId") String orderId) {
        log.info("Before call order-service  microservice (getOrder)");
        OrderDto orderDto = orderService.getOrderByOrderId(orderId);

        ResponseOrder result = mapper.map(orderDto, ResponseOrder.class);
        log.info("After call order-service  microservice (getOrder)");
        return ResponseEntity.ok(result);
    }
}
