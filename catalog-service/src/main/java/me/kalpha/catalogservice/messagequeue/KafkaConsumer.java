package me.kalpha.catalogservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.kalpha.catalogservice.jpa.CatalogEntity;
import me.kalpha.catalogservice.jpa.CatalogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KafkaConsumer {
    private CatalogRepository catalogRepository;

    @Autowired
    public KafkaConsumer(CatalogRepository catalogRepository) {
        this.catalogRepository = catalogRepository;
    }

    @KafkaListener(topics = "example-catalog-topic")
    public void updateQty(String kafkaMessage) {
        log.info("Kafka Message : " + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        /* Listen 된 Topic 메시지를 Map에 담기 */
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        CatalogEntity catalogEntity = catalogRepository.findByProductId((String) map.get("productId"));
        if (catalogEntity == null) {
            log.error("productId not found : " + (String) map.get("productId"));
        } else {
            catalogEntity.setStock(catalogEntity.getStock() - (Integer) map.get("qty"));
            catalogRepository.save(catalogEntity);
        }
    }
}
