package com.example.productservice.category;

import com.example.productservice.category.exception.CategoryAlreadyExistsException;
import com.example.productservice.category.exception.CategoryNotFoundException;
import com.example.productservice.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private CategoryService categoryService;

    @Test
    void createNewCategory_CategoryDoesNotExist_SuccessfullyCreated() {
        // Given
        String categoryName = "NewCategory";
        when(categoryRepository.existsByCategoryName(categoryName)).thenReturn(false);

        // When
        categoryService.createNewCategory(categoryName);

        // Then
        verify(categoryRepository, times(1)).existsByCategoryName(categoryName);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void createNewCategory_CategoryAlreadyExists_ThrowsCategoryAlreadyExistsException() {
        // Given
        String categoryName = "ExistingCategory";
        when(categoryRepository.existsByCategoryName(categoryName)).thenReturn(true);

        // When/Then
        assertThrows(CategoryAlreadyExistsException.class, () -> categoryService.createNewCategory(categoryName));

        verify(categoryRepository, times(1)).existsByCategoryName(categoryName);
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void getAllCategories_CategoriesExist_ReturnsListOfCategoryNames() {
        // Given
        List<Category> categories = List.of(
                new Category(1L, "Category1"),
                new Category(2L, "Category2")
        );
        when(categoryRepository.findAll()).thenReturn(categories);

        // When
        List<String> result = categoryService.getAllCategories();

        // Then
        assertEquals(2, result.size());
        assertEquals("Category1", result.get(0));
        assertEquals("Category2", result.get(1));
    }

    @Test
    void deleteCategory_CategoryExists_SuccessfullyDeleted() {
        // Given
        Long categoryId = 1L;
        Category category = new Category(categoryId, "CategoryToDelete");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.existsProductByCategory_Id(categoryId)).thenReturn(false);

        // When
        categoryService.deleteCategory(categoryId);

        // Then
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(productRepository, times(1)).existsProductByCategory_Id(categoryId);
        verify(categoryRepository, times(1)).delete(category);
    }

    @Test
    void deleteCategory_ProductsInCategory_ThrowsIllegalArgumentException() {
        // Given
        Long categoryId = 1L;
        Category category = new Category(categoryId, "CategoryToDelete");
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.existsProductByCategory_Id(categoryId)).thenReturn(true);

        // When/Then
        assertThrows(IllegalArgumentException.class, () -> categoryService.deleteCategory(categoryId));
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(productRepository, times(1)).existsProductByCategory_Id(categoryId);
        verify(categoryRepository, never()).delete(any(Category.class));


    }

    @Test
    void deleteCategory_CategoryNotFound_ThrowsCategoryNotFoundException() {
        // Given
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteCategory(categoryId));

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, never()).delete(any(Category.class));
    }
}