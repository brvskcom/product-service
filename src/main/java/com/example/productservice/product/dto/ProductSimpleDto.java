package com.example.productservice.product.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductSimpleDto {
    private String productName;
    private BigDecimal price;
    private int amountOfProducts;
    private Long id;
}
