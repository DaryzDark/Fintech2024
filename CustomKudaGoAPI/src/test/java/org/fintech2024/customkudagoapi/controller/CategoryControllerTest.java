package org.fintech2024.customkudagoapi.controller;

import org.fintech2024.customkudagoapi.exeption.EntityNotFoundException;
import org.fintech2024.customkudagoapi.model.Category;
import org.fintech2024.customkudagoapi.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    private Category mockCategory;

    @BeforeEach
    void setUp() {
        mockCategory = new Category("music", "Music");
    }

    @Test
    void getAllCategories_ReturnsCategoryList_Positive() throws Exception {
        List<Category> mockCategories = List.of(mockCategory);
        when(categoryService.getAllCategories()).thenReturn(mockCategories);

        mockMvc.perform(get("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(mockCategories.size()))
                .andExpect(jsonPath("$[0].slug").value("music"))
                .andExpect(jsonPath("$[0].name").value("Music"));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void getAllCategories_ReturnsEmptyList_Negative() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));

        verify(categoryService, times(1)).getAllCategories();
    }

    @Test
    void getCategoryById_ReturnsCategory_Positive() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.of(mockCategory));

        mockMvc.perform(get("/api/v1/places/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value("music"))
                .andExpect(jsonPath("$.name").value("Music"));

        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    void getCategoryById_ReturnsNotFound_Negative() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/places/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(categoryService, times(1)).getCategoryById(1L);
    }

    @Test
    void createCategory_ReturnsCreatedCategory_Positive() throws Exception {
        when(categoryService.addCategory(any(Category.class))).thenReturn(mockCategory);

        mockMvc.perform(post("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"slug\": \"music\", \"name\": \"Music\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.slug").value("music"))
                .andExpect(jsonPath("$.name").value("Music"));

        verify(categoryService, times(1)).addCategory(any(Category.class));
    }

    @Test
    void createCategory_ReturnsBadRequest_Negative() throws Exception {
        mockMvc.perform(post("/api/v1/places/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Music\"}")) // Отсутствует поле slug
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).addCategory(any(Category.class));
    }

    @Test
    void updateCategory_ReturnsUpdatedCategory_Positive() throws Exception {
        Category updatedCategory = new Category("music", "Updated Music");
        when(categoryService.updateCategory(eq(1L), any(Category.class))).thenReturn(Optional.of(updatedCategory));

        mockMvc.perform(put("/api/v1/places/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"slug\": \"music\", \"name\": \"Updated Music\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.slug").value("music"))
                .andExpect(jsonPath("$.name").value("Updated Music"));

        verify(categoryService, times(1)).updateCategory(eq(1L), any(Category.class));
    }

    @Test
    void updateCategory_ReturnsNotFound_Negative() throws Exception {
        when(categoryService.updateCategory(eq(1L), any(Category.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/places/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"slug\": \"music\", \"name\": \"Updated Music\"}"))
                .andExpect(status().isNotFound());

        verify(categoryService, times(1)).updateCategory(eq(1L), any(Category.class));
    }

    @Test
    void deleteCategory_ReturnsNoContent_Positive() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/api/v1/places/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(categoryService, times(1)).deleteCategory(1L);
    }

    @Test
    void deleteCategory_ReturnsNotFound_Negative() throws Exception {
        doThrow(new EntityNotFoundException(1L)).when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/api/v1/places/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(categoryService, times(1)).deleteCategory(1L);
    }
}