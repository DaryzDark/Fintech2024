package org.fintech2024.customkudagoapi.pattern.memento;

import org.fintech2024.customkudagoapi.model.Category;
import org.hibernate.annotations.Comment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

@Component
public class CategoryHistory {
    private final Map<Long, Stack<CategoryMemento>> history = new HashMap<>();

    public void save(Long categoryId, Category category) {
        history.putIfAbsent(categoryId, new Stack<>());
        history.get(categoryId).push(new CategoryMemento(category));
    }

    public CategoryMemento undo(Long categoryId) {
        Stack<CategoryMemento> categoryHistory = history.get(categoryId);
        if (categoryHistory == null || categoryHistory.isEmpty()) {
            return null;
        }
        return categoryHistory.pop();
    }
}