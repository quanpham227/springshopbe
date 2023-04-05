package com.springshopbe.service;

import com.springshopbe.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICategoryService {
    Page<CategoryDTO> getAllCategoryPaginged (Pageable pageable);
    Page<CategoryDTO> searchCategoryPaginged(String name, Pageable pageable);
    CategoryDTO findByCategoryId(Long id);
    CategoryDTO findByCategoryName(String name);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(CategoryDTO categoryDTO);
    void deleteCategoryById(Long id);

}
