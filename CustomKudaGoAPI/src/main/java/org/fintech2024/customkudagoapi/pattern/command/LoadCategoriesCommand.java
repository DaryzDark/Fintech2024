package org.fintech2024.customkudagoapi.pattern.command;

import lombok.extern.slf4j.Slf4j;
import org.fintech2024.customkudagoapi.model.Category;
import org.fintech2024.customkudagoapi.repository.InMemoryGenericDataStore;
import org.fintech2024.customkudagoapi.service.FetchKudaGoAPIService;

import java.util.List;

@Slf4j
public class LoadCategoriesCommand implements Command {

    private final FetchKudaGoAPIService apiService;
    private final InMemoryGenericDataStore<Category> categoryDataStore;

    public LoadCategoriesCommand(FetchKudaGoAPIService apiService, InMemoryGenericDataStore<Category> categoryDataStore) {
        this.apiService = apiService;
        this.categoryDataStore = categoryDataStore;
    }

    @Override
    public void execute() {
        try {
            log.info("Request categories from the KudaGo API...");
            List<Category> categories = apiService.fetchCategories();
            categories.forEach(categoryDataStore::add);
            log.info("The categories have been successfully uploaded and saved. Total categories: {}", categories.size());
        } catch (Exception e) {
            log.error("Categories load error: {}", e.getMessage());
        }
    }
}