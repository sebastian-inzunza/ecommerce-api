package com.ecom.ecom.services.adminCategory;

import org.springframework.stereotype.Service;

import com.ecom.ecom.dto.CategoryDto;
import com.ecom.ecom.entity.Category;

@Service
public interface  CategoryService {

  Category createCategory(CategoryDto categoryDto);

}