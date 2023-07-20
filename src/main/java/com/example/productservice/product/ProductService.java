package com.example.productservice.product;

import com.example.productservice.category.Category;
import com.example.productservice.category.CategoryRepository;
import com.example.productservice.category.exception.CategoryNotFoundException;
import com.example.productservice.product.dto.ProductDetailedDto;
import com.example.productservice.product.dto.ProductDtoMapper;
import com.example.productservice.product.dto.ProductSimpleDto;
import com.example.productservice.product.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductDtoMapper productDtoMapper;

    public void createNewProduct(ProductCreateCommand command){
        Category category = categoryRepository.findByCategoryName(command.getCategoryName())
                .orElseThrow(() -> new CategoryNotFoundException(command.getCategoryName()));

        Product newProduct = toEntity(command);
        newProduct.setCategory(category);

        productRepository.save(newProduct);
    }

    @Transactional
    public void deleteProduct(Long productId){
        Product productToDelete = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        productRepository.delete(productToDelete);
    }

    public Page<ProductSimpleDto> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(productDtoMapper::toSimpleDto);
    }

    public Page<ProductSimpleDto> getProductsByCategoryName(String categoryName, Pageable pageable) {
        categoryRepository.findByCategoryName(categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(categoryName));

        Page<Product> productPage = productRepository.getProductsByCategory_CategoryName(categoryName, pageable);
        return productPage.map(productDtoMapper::toSimpleDto);
    }

    public ProductDetailedDto getDetailedProductInfo(Long productId){
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return productDtoMapper.toDetailedDto(product);
    }

    public void updateProductDescription(Long productId, String newDescription) {
        Product productToUpdate = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        productToUpdate.setDescription(newDescription);
        productRepository.save(productToUpdate);
    }

    @Transactional
    public void increaseAmountOfProducts(Long productId, int increaseByAmount){
        Product productToUpdate = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        int currentAmount = productToUpdate.getAmountOfProducts();
        int newAmount = currentAmount + increaseByAmount;
        productToUpdate.setAmountOfProducts(newAmount);

        productRepository.save(productToUpdate);
    }

    @Transactional
    public void decreaseAmountOfProducts(Long productId, int decreaseByAmount) {
        Product productToUpdate = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        int currentAmount = productToUpdate.getAmountOfProducts();
        int newAmount = currentAmount - decreaseByAmount;

        if (newAmount < 0) {
            throw new IllegalArgumentException("The amount of products cannot be negative.");
        }

        productToUpdate.setAmountOfProducts(newAmount);
        productRepository.save(productToUpdate);
    }


    private Product toEntity(ProductCreateCommand command){
        return Product
                .builder()
                .name(command.getProductName())
                .price(command.getPrice())
                .description(command.getDescription())
                .amountOfProducts(command.getAmountOfProducts())
                .build();
    }
}
