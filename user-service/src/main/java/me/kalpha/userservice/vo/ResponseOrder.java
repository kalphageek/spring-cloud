package me.kalpha.userservice.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseOrder {
    private String productId;
    private Integer qty;
    private Integer unitPrice;
    private Integer totalAmount;
    private Date createdAt;

    private String orderId;
}
