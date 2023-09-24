package com.example.productservice.product;

import com.example.productservice.category.Category;
import com.example.productservice.category.CategoryRepository;
import com.example.productservice.category.exception.CategoryNotFoundException;
import com.example.productservice.product.dto.ProductDetailedDto;
import com.example.productservice.product.dto.ProductDtoMapper;
import com.example.productservice.product.dto.ProductSimpleDto;
import com.example.productservice.product.exception.ProductNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductDtoMapper productDtoMapper;

    @InjectMocks
    private ProductService productService;

    @Test
    public void createNewProduct_ValidCommand_ProductSavedSuccessfully() {
        // Given
        ProductCreateCommand command = createCommand();

        Category category = createCategory();

        Product newProduct = createProduct(1L);
        newProduct.setCategory(category);

        when(categoryRepository.findByCategoryName("Test Category")).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);

        // When
        productService.createNewProduct(command);

        // Then
        verify(categoryRepository).findByCategoryName("Test Category");
        verify(productRepository).save(any(Product.class));
    }

    @Test
    public void createNewProduct_CategoryNotFound_ExceptionThrown() {
        // Given
        ProductCreateCommand command = createCommand();
        command.setCategoryName("Nonexistent Category");

        when(categoryRepository.findByCategoryName("Nonexistent Category")).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(CategoryNotFoundException.class, () -> {
            productService.createNewProduct(command);
        });

    }

    @Test
    public void deleteProduct_ProductExists_ProductDeletedSuccessfully() {
        // Given
        Product productToDelete = createProduct(1L);
        Long productId = productToDelete.getId();

        when(productRepository.findById(productId)).thenReturn(Optional.of(productToDelete));

        // When
        productService.deleteProduct(productId);

        // Then
        verify(productRepository).findById(productId);
        verify(productRepository).delete(productToDelete);
    }

    @Test
    public void deleteProduct_ProductNotFound_ExceptionThrown() {
        // Given
        long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(ProductNotFoundException.class, () -> {
            productService.deleteProduct(productId);
        });
    }

    @Test
    public void getAllProducts_ReturnsListOfProductSimpleDtos() {
        // Given
        List<Product> productList = Arrays.asList(
                createProduct(1L),
                createProduct(2L)
        );

        List<ProductSimpleDto> productSimpleDtoList = Arrays.asList(
                createProductSimpleDto(1L),
                createProductSimpleDto(2L)
        );

        Pageable pageable = Pageable.unpaged();
        Page<Product> productPage = new PageImpl<>(productList, pageable, productList.size());

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productDtoMapper.toSimpleDto(any(Product.class))).thenReturn(productSimpleDtoList.get(0), productSimpleDtoList.get(1));

        // When
        Page<ProductSimpleDto> result = productService.getAllProducts(pageable);

        // Then
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals(2L, result.getContent().get(1).getId());
    }

    @Test
    public void getDetailedProductInfo_ProductExists_ReturnsProductDetailedDto() {
        // Given
        Long productId = 1L;
        Product product = createProduct(productId);

        ProductDetailedDto expectedDto = createProductDetailedDto(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productDtoMapper.toDetailedDto(any(Product.class))).thenReturn(expectedDto);

        // When
        ProductDetailedDto result = productService.getDetailedProductInfo(productId);

        // Then
        assertEquals(expectedDto, result);
    }

    @Test
    public void getDetailedProductInfo_ProductDoesNotExist_ThrowsProductNotFoundException() {
        // Given
        Long productId = 1L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When
        // Then
        assertThrows(ProductNotFoundException.class, () -> productService.getDetailedProductInfo(productId));
    }

    @Test
    public void updateProductDescription_ProductExists_DescriptionUpdated() {
        // Given
        Long productId = 1L;
        String newDescription = "updated description.";

        Product product = createProduct(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // When
        productService.updateProductDescription(productId, newDescription);

        // Then
        assertEquals(newDescription, product.getDescription());
    }

    @Test
    public void updateProductDescription_ProductDoesNotExist_ThrowsProductNotFoundException() {
        // Given
        Long productId = 1L;
        String newDescription = "updated description.";

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ProductNotFoundException.class, () -> productService.updateProductDescription(productId, newDescription));
    }

    @Test
    public void increaseAmountOfProducts_ProductFound_AmountIncreased() {
        // Given
        Long productId = 1L;
        int increaseByAmount = 5;
        Product product = createProduct(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // When
        productService.increaseAmountOfProducts(productId, increaseByAmount);

        // Then
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(product);
        assertEquals(55, product.getUnitsInStock());
    }

    @Test
    public void decreaseAmountOfProducts_ProductFound_AmountDecreased() {
        // Given
        Long productId = 1L;
        int decreaseByAmount = 3;
        Product product = createProduct(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // When
        productService.decreaseAmountOfProducts(productId, decreaseByAmount);

        // Then
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).save(product);
        assertEquals(47, product.getUnitsInStock());
    }

    @Test
    public void decreaseAmountOfProducts_ProductFound_NewAmountUnderZero_ExceptionThrown() {
        // Given
        Long productId = 1L;
        int decreaseByAmount = 11;
        Product product = Product.builder()
                .id(productId)
                .unitsInStock(10)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // When
        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.decreaseAmountOfProducts(productId, decreaseByAmount);
        });
        assertEquals("The amount of products cannot be negative.", exception.getMessage());
    }

    @Test
    public void increaseAmountOfProducts_ProductNotFound_ExceptionThrown() {
        // Given
        Long productId = 1L;
        int increaseByAmount = 5;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ProductNotFoundException.class, () -> {
            productService.increaseAmountOfProducts(productId, increaseByAmount);
        });
    }


    private ProductDetailedDto createProductDetailedDto(Long productId){
        return ProductDetailedDto
                .builder()
                .id(productId)
                .amountOfProducts(50)
                .price(BigDecimal.valueOf(100.0))
                .productName("Test Product")
                .categoryName("Test Category")
                .description("Test Description")
                .build();
    }

    private ProductSimpleDto createProductSimpleDto(Long productId){
        return ProductSimpleDto
                .builder()
                .id(productId)
                .amountOfProducts(50)
                .price(BigDecimal.valueOf(100.0))
                .productName("Test Product")
                .build();
    }


    private Product createProduct(Long productId){
        return Product
                .builder()
                .id(productId)
                .name("Test Product")
                .price(BigDecimal.valueOf(100.0))
                .description("Test Description")
                .unitsInStock(50)
                .build();
    }

    private ProductCreateCommand createCommand(){
        return ProductCreateCommand
                .builder()
                .productName("Test Product")
                .price(BigDecimal.valueOf(100.0))
                .categoryName("Test Category")
                .description("Test Description")
                .amountOfProducts(50)
                .build();
    }

    private Category createCategory(){
        return new Category(1L, "Test Category");
    }

}