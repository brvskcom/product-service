package com.example.productservice.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class ProductDetailedDto {
    private String productName;
    private String description;
    private String categoryName;
    private int amountOfProducts;
    private BigDecimal price;
    private Long id;
}
