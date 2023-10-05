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

    public Page<ProductSimpleDto> getProductsByCategoryId(Long categoryId, Pageable pageable) {
        categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        Page<Product> productPage = productRepository.getProductsByCategory_Id(categoryId, pageable);
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

        int currentAmount = productToUpdate.getUnitsInStock();
        int newAmount = currentAmount + increaseByAmount;
        productToUpdate.setUnitsInStock(newAmount);

        productRepository.save(productToUpdate);
    }

    @Transactional
    public void decreaseAmountOfProducts(Long productId, int decreaseByAmount) {
        Product productToUpdate = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        int currentAmount = productToUpdate.getUnitsInStock();
        int newAmount = currentAmount - decreaseByAmount;

        productToUpdate.setUnitsInStock(newAmount);
        productRepository.save(productToUpdate);
    }


    private Product toEntity(ProductCreateCommand command){
        return Product
                .builder()
                .name(command.getProductName())
                .price(command.getUnitPrice())
                .description(command.getDescription())
                .unitsInStock(command.getUnitsInStock())
                .imageUrl(command.getImageUrl())
                .sku(command.getSku())
                .build();
    }
}
