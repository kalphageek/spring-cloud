package me.kalpha.orderservice.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestOrder {
    @NotNull(message = "productId가 필요합니다.")
    private String productId;
    @NotNull(message = "qty가 필요합니다.")
    @Size(min = 1, message = "qty가 1 이상이어야 합니닫.")
    private Integer qty;
    @NotNull(message = "unitPrice가 필요합니다.")
    @Size(min = 1, message = "unitPrice가 1 이상이어야 합니다.")
    private Integer unitPrice;
}
