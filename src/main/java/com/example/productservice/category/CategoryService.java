package com.example.productservice.category;


import com.example.productservice.category.exception.CategoryAlreadyExistsException;
import com.example.productservice.category.exception.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

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
//         TODO: 7/12/2023 check if there is any item in this category
        categoryRepository.delete(categoryToDelete);
    }



}
