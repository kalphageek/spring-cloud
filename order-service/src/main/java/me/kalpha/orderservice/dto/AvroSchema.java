package me.kalpha.orderservice.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AvroSchema {
    private String type;
    private String name;
    private List<Field> fields;
}
