package me.kalpha.orderservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.kalpha.orderservice.dto.*;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderProducer {
    private KafkaTemplate<String, GenericRecord> kafkaTemplate;
    List<Field> fields = Arrays.asList(new Field("order_id", "string"),
            new  Field( "user_id", "string"),
            new  Field( "product_id", "string"),
            new  Field( "qty", "int"),
            new  Field( "unit_price", "int"),
            new  Field( "total_amount", "int"));
    AvroSchema avroSchema = AvroSchema.builder()
            .type("record")
            .name("orders")
            .fields(fields)
            .build();

    @Autowired
    public OrderProducer(KafkaTemplate<String, GenericRecord> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public OrderDto send(String topic, OrderDto orderDto) {
        String orderId = UUID.randomUUID().toString();

        ObjectMapper mapper = new ObjectMapper();
        String schemaString = "";
        try {
            schemaString = mapper.writeValueAsString(avroSchema);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Schema.Parser parser = new Schema.Parser();
        Schema avroSchema = parser.parse(schemaString);
        GenericRecord avroRecord = new GenericData.Record(avroSchema);

        avroRecord.put("order_id", orderId);
        avroRecord.put("product_id", orderDto.getProductId());
        avroRecord.put("user_id", orderDto.getUserId());
        avroRecord.put("qty", orderDto.getQty());
        avroRecord.put("unit_price", orderDto.getUnitPrice());
        avroRecord.put("total_amount", orderDto.getQty() * orderDto.getUnitPrice());

        try {
            kafkaTemplate.send(new ProducerRecord<>(topic, orderId, avroRecord));
            log.info("Order producer sent data from Order microservice : " + avroRecord.toString());
        } catch (SerializationException e) {
            e.printStackTrace();
        }
        return orderDto;
    }
}
