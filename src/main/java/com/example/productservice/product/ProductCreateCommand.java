package com.example.productservice.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@Data
public class ProductCreateCommand {
    @NotBlank(message = "Product name cannot be blank")
    private String productName;
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;
    @NotBlank(message = "Category name cannot be blank")
    private String categoryName;
    @NotBlank(message = "Description cannot be blank")
    private String description;
    @Min(value = 0, message = "Amount of products must be greater than or equal to 0")
    private int amountOfProducts;
}
