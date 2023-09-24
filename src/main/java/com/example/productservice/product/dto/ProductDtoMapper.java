package com.example.productservice.product.dto;

import com.example.productservice.product.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductDtoMapper {

    public ProductSimpleDto toSimpleDto(Product product){
        return ProductSimpleDto
                .builder()
                .productName(product.getName())
                .unitPrice(product.getPrice())
                .unitsInStock(product.getUnitsInStock())
                .id(product.getId())
                .imageUrl(product.getImageUrl())
                .build();
    }

    public ProductDetailedDto toDetailedDto(Product product){
        return ProductDetailedDto
                .builder()
                .productName(product.getName())
                .description(product.getDescription())
                .unitsInStock(product.getUnitsInStock())
                .unitPrice(product.getPrice())
                .id(product.getId())
                .sku(product.getSku())
                .imageUrl(product.getImageUrl())
                .updatedAt(product.getUpdatedAt())
                .createdAt(product.getCratedAt())
                .build();
    }


}
