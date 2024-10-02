package org.fintech2024.customkudagoapi.service;

import org.fintech2024.customkudagoapi.model.Category;
import org.fintech2024.customkudagoapi.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCategories_ReturnsCategoryList_Positive() {
        List<Category> mockCategories = List.of(
                new Category("music", "Music"),
                new Category("art", "Art")
        );
        when(categoryRepository.getAll()).thenReturn(mockCategories);

        List<Category> result = categoryService.getAllCategories();

        assertEquals(mockCategories, result);
        verify(categoryRepository, times(1)).getAll();
    }

    @Test
    void getAllCategories_ReturnsEmptyList_Negative() {
        when(categoryRepository.getAll()).thenReturn(List.of());

        List<Category> result = categoryService.getAllCategories();

        assertTrue(result.isEmpty());
        verify(categoryRepository, times(1)).getAll();
    }

    @Test
    void getCategoryById_ReturnsCategory_Positive() {
        Category mockCategory = new Category("music", "Music");
        when(categoryRepository.getById(1L)).thenReturn(Optional.of(mockCategory));

        Optional<Category> result = categoryService.getCategoryById(1L);

        assertTrue(result.isPresent());
        assertEquals(mockCategory, result.get());
        verify(categoryRepository, times(1)).getById(1L);
    }

    @Test
    void getCategoryById_ReturnsEmpty_Negative() {
        when(categoryRepository.getById(1L)).thenReturn(Optional.empty());

        Optional<Category> result = categoryService.getCategoryById(1L);

        assertFalse(result.isPresent());
        verify(categoryRepository, times(1)).getById(1L);
    }

    @Test
    void addCategory_SavesCategory_Positive() {
        Category mockCategory = new Category("music", "Music");
        when(categoryRepository.add(mockCategory)).thenReturn(mockCategory);

        Category result = categoryService.addCategory(mockCategory);

        assertEquals(mockCategory, result);
        verify(categoryRepository, times(1)).add(mockCategory);
    }

    @Test
    void addCategory_ThrowsExceptionForExistingSlug_Negative() {
        Category mockCategory = new Category("music", "Music");
        when(categoryRepository.add(mockCategory)).thenThrow(new IllegalArgumentException("Category with this slug already exists"));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> categoryService.addCategory(mockCategory));

        assertEquals("Category with this slug already exists", exception.getMessage());
        verify(categoryRepository, times(1)).add(mockCategory);
    }

    @Test
    void updateCategory_UpdatesCategory_Positive() {
        Category updatedCategory = new Category("music", "Updated Music");
        when(categoryRepository.update(1L, updatedCategory)).thenReturn(Optional.of(updatedCategory));

        Optional<Category> result = categoryService.updateCategory(1L, updatedCategory);

        assertTrue(result.isPresent());
        assertEquals(updatedCategory, result.get());
        verify(categoryRepository, times(1)).update(1L, updatedCategory);
    }

    @Test
    void updateCategory_ReturnsEmpty_Negative() {
        Category updatedCategory = new Category("music", "Updated Music");
        when(categoryRepository.update(1L, updatedCategory)).thenReturn(Optional.empty());

        Optional<Category> result = categoryService.updateCategory(1L, updatedCategory);

        assertFalse(result.isPresent());
        verify(categoryRepository, times(1)).update(1L, updatedCategory);
    }

    @Test
    void deleteCategory_DeletesCategory_Positive() {
        doNothing().when(categoryRepository).delete(1L);

        categoryService.deleteCategory(1L);

        verify(categoryRepository, times(1)).delete(1L);
    }

    @Test
    void deleteCategory_ThrowsException_Negative() {
        doThrow(new IllegalArgumentException("Category not found")).when(categoryRepository).delete(1L);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> categoryService.deleteCategory(1L));

        assertEquals("Category not found", exception.getMessage());
        verify(categoryRepository, times(1)).delete(1L);
    }

}