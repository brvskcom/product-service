package com.example.productservice.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<String> getAllCategories(){
        return categoryService.getAllCategories();
    }

    @PostMapping
    public ResponseEntity<String> createNewCategory(@RequestBody String categoryName){
        categoryService.createNewCategory(categoryName);
        String responseMessage = "A new category " + categoryName + " has been added";
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMessage);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteCategory(@RequestBody Long categoryId){
        categoryService.deleteCategory(categoryId);
        String responseMessage = "Category with id "+categoryId+ " has been deleted";
        return ResponseEntity.status(HttpStatus.OK).body(responseMessage);
    }
}
