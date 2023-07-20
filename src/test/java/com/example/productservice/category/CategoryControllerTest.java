package com.example.productservice.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    void getAllCategories_ReturnsListOfCategories() throws Exception {
        // Given
        List<String> categories = List.of("Category1", "Category2");
        when(categoryService.getAllCategories()).thenReturn(categories);

        // When/Then
        mockMvc.perform(get("/api/v1/categories"))
                .andExpect(status().isOk())
                .andExpect(content().json("[\"Category1\",\"Category2\"]"));
    }

    @Test
    void createNewCategory_ValidCategory_ReturnsCreatedStatus() throws Exception {
        // Given
        String categoryName = "NewCategory";

        // When/Then
        mockMvc.perform(post("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryName))
                .andExpect(status().isCreated())
                .andExpect(content().string("A new category NewCategory has been added"));

        verify(categoryService, times(1)).createNewCategory(categoryName);
    }

    @Test
    void deleteCategory_ValidCategoryId_ReturnsOkStatus() throws Exception {
        // Given
        Long categoryId = 1L;

        // When/Then
        mockMvc.perform(delete("/api/v1/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(categoryId)))
                .andExpect(status().isOk())
                .andExpect(content().string("Category with id 1 has been deleted"));

        verify(categoryService, times(1)).deleteCategory(categoryId);
    }
}