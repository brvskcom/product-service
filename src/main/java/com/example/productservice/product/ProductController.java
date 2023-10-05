package com.example.productservice.product;

import com.example.productservice.product.dto.ProductDetailedDto;
import com.example.productservice.product.dto.ProductSimpleDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> createNewProduct(@RequestBody @Valid ProductCreateCommand command){
        productService.createNewProduct(command);
        String responseMessage = "A new product " + command.getProductName() + " has been added";
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    @DeleteMapping(path = "/{product_id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long product_id){
        productService.deleteProduct(product_id);
        String responseMessage = "Product with ID "+product_id+ " has been deleted";
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @GetMapping
    public Page<ProductSimpleDto> getAllProducts(@PageableDefault(size = 20) Pageable pageable) {
        return productService.getAllProducts(pageable);
    }

    @GetMapping(value = "/search/findByCategoryId", params = "id")
    public Page<ProductSimpleDto> getProductsByCategoryId(@RequestParam(name = "id") Long categoryId,
                                                            @PageableDefault(size = 20) Pageable pageable) {
        return productService.getProductsByCategoryId(categoryId, pageable);
    }

    @GetMapping(path = "/{product_id}")
    public ProductDetailedDto getDetailedProductInfo(@PathVariable Long product_id){
        return productService.getDetailedProductInfo(product_id);
    }

    @PatchMapping("/{product_id}/description")
    public ResponseEntity<String> updateProductDescription(@PathVariable Long product_id, @RequestBody String newDescription) {
        productService.updateProductDescription(product_id, newDescription);
        String responseMessage = "Description of product with ID " + product_id + " has been updated";
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @PutMapping("/{product_id}/increase")
    public ResponseEntity<String> increaseAmountOfProducts(@PathVariable("product_id") Long productId,
                                                           @RequestParam("amount") int increaseByAmount) {
        productService.increaseAmountOfProducts(productId, increaseByAmount);
        String responseMessage = "Amount of product with ID " + productId + " has been increased by " + increaseByAmount;
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

    @PutMapping("/{product_id}/decrease")
    public ResponseEntity<String> decreaseAmountOfProducts(@PathVariable("product_id") Long productId,
                                                           @RequestParam("amount") int decreaseByAmount) {
        productService.decreaseAmountOfProducts(productId, decreaseByAmount);
        String responseMessage = "Amount of product with ID " + productId + " has been decreased by " + decreaseByAmount;
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }

}
