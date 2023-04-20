package com.springshopbe.service;

import com.springshopbe.dto.CategoryDTO;
import com.springshopbe.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService {
    List<CategoryDTO> getAllCategories();
    Page<CategoryEntity> searchCategoryPaginged(String name, Pageable pageable);
    CategoryDTO findByCategoryId(Long id);
    CategoryDTO findByCategoryName(String name);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO);
    void deleteCategoryById(Long id);

}
