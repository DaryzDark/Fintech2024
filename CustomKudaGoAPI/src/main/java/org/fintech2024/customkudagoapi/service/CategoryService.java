package org.fintech2024.customkudagoapi.service;

import org.fintech2024.customkudagoapi.model.Category;
import org.fintech2024.customkudagoapi.pattern.memento.CategoryHistory;
import org.fintech2024.customkudagoapi.pattern.memento.CategoryMemento;
import org.fintech2024.customkudagoapi.pattern.observer.Observer;
import org.fintech2024.customkudagoapi.pattern.observer.Subject;
import org.fintech2024.customkudagoapi.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements Subject {

    private final CategoryRepository categoryRepository;
    private final List<Observer> observers = new ArrayList<>();
    private final CategoryHistory categoryHistory;

    public CategoryService(CategoryRepository categoryRepository, CategoryHistory categoryHistory) {
        this.categoryRepository = categoryRepository;
        this.categoryHistory = categoryHistory;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public List<Category> getAllCategories() {
        return categoryRepository.getAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.getById(id);
    }

    public Category addCategory(Category category) {
        Category addedCategory = categoryRepository.add(category);
        notifyObservers("Category added: " + addedCategory);
        return addedCategory;
    }

    public Optional<Category> updateCategory(Long id, Category category) {
        Optional<Category> existingCategory = categoryRepository.getById(id);
        existingCategory.ifPresent(cat -> categoryHistory.save(id, cat));
        Optional<Category> updatedCategory = categoryRepository.update(id, category);
        updatedCategory.ifPresent(cat -> notifyObservers("Category updated: " + cat));
        return updatedCategory;
    }

    public void deleteCategory(Long id) {
        categoryRepository.getById(id).ifPresent(cat -> {
            categoryHistory.save(id, cat);
            categoryRepository.delete(id);
            notifyObservers("Category deleted: " + cat);
        });
    }

    public Optional<Category> restorePreviousState(Long id) {
        CategoryMemento memento = categoryHistory.undo(id);
        if (memento != null) {
            Category previousState = memento.getState();
            categoryRepository.update(id, previousState);
            return Optional.of(previousState);
        }
        return Optional.empty();
    }
}
