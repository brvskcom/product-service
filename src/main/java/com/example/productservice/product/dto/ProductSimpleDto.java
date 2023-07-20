package com.example.productservice.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class ProductSimpleDto {
    private String productName;
    private BigDecimal price;
    private int amountOfProducts;
    private Long id;
}
