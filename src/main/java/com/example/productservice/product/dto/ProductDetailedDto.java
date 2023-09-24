package com.example.productservice.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ProductDetailedDto {
    private String productName;
    private String description;
    private int unitsInStock;
    private BigDecimal unitPrice;
    private Long id;
    private String sku;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
