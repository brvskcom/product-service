package com.example.productservice.category;


import com.example.productservice.category.exception.CategoryAlreadyExistsException;
import com.example.productservice.category.exception.CategoryNotFoundException;
import com.example.productservice.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public void createNewCategory(String categoryName){
        if (categoryRepository.existsByCategoryName(categoryName)){
            throw new CategoryAlreadyExistsException(categoryName);
        }

        Category newCategory = new Category(categoryName);
        categoryRepository.save(newCategory);
    }
    
    public List<String> getAllCategories(){
        return categoryRepository
                .findAll()
                .stream()
                .map(Category::getCategoryName)
                .collect(Collectors.toList());
    }
    
    public void deleteCategory(Long categoryId){
        Category categoryToDelete = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));

        if (productRepository.existsProductByCategory_Id(categoryId)) {
            throw new IllegalArgumentException("Cannot delete category with ID " + categoryId + ". " +
                    "You have to delete products associated with this category first");
        }
        categoryRepository.delete(categoryToDelete);
    }



}
