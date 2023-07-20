package com.example.productservice.product.dto;

import com.example.productservice.product.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductDtoMapper {

    public ProductSimpleDto toSimpleDto(Product product){
        return ProductSimpleDto
                .builder()
                .productName(product.getName())
                .price(product.getPrice())
                .amountOfProducts(product.getAmountOfProducts())
                .id(product.getId())
                .build();
    }

    public ProductDetailedDto toDetailedDto(Product product){
        return ProductDetailedDto
                .builder()
                .productName(product.getName())
                .description(product.getDescription())
                .categoryName(product.getCategory().getCategoryName())
                .amountOfProducts(product.getAmountOfProducts())
                .price(product.getPrice())
                .id(product.getId())
                .build();
    }


}
