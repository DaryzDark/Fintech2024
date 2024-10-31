package org.fintech2024.customkudagoapi.pattern.memento;

import org.fintech2024.customkudagoapi.model.Category;

public class CategoryMemento implements Memento<Category> {
    private final Category state;

    public CategoryMemento(Category state) {
        this.state = new Category(state.getSlug(), state.getName());
    }

    @Override
    public Category getState() {
        return state;
    }
}
