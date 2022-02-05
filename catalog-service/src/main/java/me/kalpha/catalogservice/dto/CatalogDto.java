package me.kalpha.catalogservice.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CatalogDto implements Serializable {
    private String productId;
    private Integer unitPrice;
    private Integer totalAmount;
    private Integer stock;

    private String orderId;
    private String userId;
}
