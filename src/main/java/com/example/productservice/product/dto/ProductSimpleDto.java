package com.example.productservice.product.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductSimpleDto {
    private String productName;
    private String imageUrl;
    private BigDecimal unitPrice;
    private int unitsInStock;
    private Long id;
}
