package com.ecom.ecom.services.adminCategory;

import org.springframework.stereotype.Service;

import com.ecom.ecom.dto.CategoryDto;
import com.ecom.ecom.entity.Category;
import com.ecom.ecom.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public Category createCategory(CategoryDto categoryDto){
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());


        return categoryRepository.save(category);
    }

}