package me.kalpha.orderservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.kalpha.orderservice.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderProducer {
    private KafkaTemplate<String, String> kafkaTemplate;
    List<Field> fields = Arrays.asList(new Field("string", true, "order_id"),
            new  Field("string", true, "user_id"),
            new  Field("string", true, "product_id"),
            new  Field("int32", true, "qty"),
            new  Field("int32", true, "unit_price"),
            new  Field("int32", true, "total_amount"));
    Schema schema = Schema.builder()
            .type("struct")
            .fields(fields)
            .optional(false)
                .name("orders")
                .build();

    @Autowired
    public OrderProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public OrderDto send(String topic, OrderDto orderDto) {
        Payload payload = Payload.builder()
                .order_id(UUID.randomUUID().toString())
                .product_id(orderDto.getProductId())
                .user_id(orderDto.getUserId())
                .qty(orderDto.getQty())
                .unit_price(orderDto.getUnitPrice())
                .total_amount(orderDto.getQty() * orderDto.getUnitPrice())
                .build();
        KafkaOrderDto kafkaOrderDto = new KafkaOrderDto(schema, payload);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(kafkaOrderDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        kafkaTemplate.send(topic, jsonString);
        log.info("Order producer sent data from Order microservice : " + kafkaOrderDto);

        return orderDto;
    }
}