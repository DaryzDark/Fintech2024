package org.fintech2024.customkudagoapi.service;

import org.fintech2024.customkudagoapi.model.Category;
import org.fintech2024.customkudagoapi.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.getAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.getById(id);
    }

    public Category addCategory(Category category) {
        return categoryRepository.add(category);
    }

    public Optional<Category> updateCategory(Long id, Category category) {
        return categoryRepository.update(id, category);
    }

    public void deleteCategory(Long id) {
         categoryRepository.delete(id);

    }
}
