package com.example.productservice.category.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends RuntimeException{

    public CategoryNotFoundException(final Long categoryId) {
        super("Category with id "+categoryId+" not found");
    }

    public CategoryNotFoundException(final String categoryName) {
        super("Category with name "+categoryName+" not found");
    }
}
