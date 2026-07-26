package com.lcwd.electronic.store.services;

import com.lcwd.electronic.store.dtos.CategoryDto;
import com.lcwd.electronic.store.response.PageableResponse;

import java.util.List;

public interface CategoryService {
    //create
    CategoryDto createCategory(CategoryDto categoryDto);

    //update
    CategoryDto updateCategory(CategoryDto categoryDto,String categoryId);

    //delete
    void deleteCategory(String categoryId);

    //get all categories
    PageableResponse<CategoryDto> getAllCategories(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir
    );

    //get a single category by id
    CategoryDto getCategory(String categoryId);
}
