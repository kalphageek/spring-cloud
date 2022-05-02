package me.kalpha.loginservice.client;

import me.kalpha.loginservice.vo.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
/* 특정 서비스만을 위한 ErrorDecoder로도 등록할 수 있다
@FeignClient(name = "order-service" configuration = OrderErrorDecoder.class)
*/
@FeignClient(name = "ORDER-SERVICE" )
public interface OrderServiceClient {
    @GetMapping("/order-service/{userId}/orders")
    List<ResponseOrder> getOrders(@PathVariable String userId);
}
