package com.springshopbe.service.impl;

import com.springshopbe.dto.CategoryDTO;
import com.springshopbe.entity.CategoryEntity;
import com.springshopbe.exeption.DuplicateRecordException;
import com.springshopbe.exeption.NotFoundExeption;
import com.springshopbe.repository.CategoryRepository;
import com.springshopbe.service.ICategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CategoryService implements ICategoryService {

    private final ModelMapper modelMapper;

    private final CategoryRepository categoryRepository;


    public CategoryService(ModelMapper modelMapper, CategoryRepository categoryRepository) {
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<CategoryDTO> categories = categoryRepository.getAllCategories().stream()
                .map(CategoryEntity -> modelMapper.map(CategoryEntity, CategoryDTO.class))
                        .collect(Collectors.toList());
        return categories;
    }

    @Override
    public Page<CategoryEntity> searchCategoryPaginged(String name, Pageable pageable) {
        return categoryRepository.searchCategoryPaginged(name, pageable);
    }

    @Override
    public CategoryDTO findByCategoryId(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findByCategoryId(id)
                .orElseThrow(()-> new NotFoundExeption("Category not found with id :" + id));
        return modelMapper.map(categoryEntity, CategoryDTO.class);
    }

    @Override
    public CategoryDTO findByCategoryName(String name) {
        CategoryEntity categoryEntity = categoryRepository.findOneByCategoryName(name)
                .orElseThrow(() -> new NotFoundExeption("Category not found with name" + name));
        return modelMapper.map(categoryEntity, CategoryDTO.class);
    }

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Optional<CategoryEntity> category = categoryRepository.findOneByCategoryName(categoryDTO.getName());
        if(category.isPresent()){
            throw new DuplicateRecordException("already exist: " + categoryDTO.getName());
        }
        try{
            CategoryEntity categoryEntity = modelMapper.map(categoryDTO, CategoryEntity.class);
            return modelMapper.map(categoryRepository.save(categoryEntity), CategoryDTO.class);
        }catch (Exception exception){
            throw new RuntimeException("Category is create fail");
        }
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) {
        CategoryEntity categoryEntityOld = this.categoryRepository.findByCategoryId(id)
                .orElseThrow(()-> new NotFoundExeption("Category not found with id :" + id));
        try{
            this.modelMapper.map(categoryDTO, categoryEntityOld);
            return modelMapper.map(this.categoryRepository.save(categoryEntityOld), CategoryDTO.class);
        }catch (Exception exception){
            throw new RuntimeException("Category is update fail");
        }

    }

    @Override
    @Transactional
    public void deleteCategoryById(Long id) {
        CategoryEntity categoryEntity = categoryRepository.findByCategoryId(id)
                .orElseThrow(() -> new NotFoundExeption("Category not found with id :" + id));
        try{
            categoryRepository.deleteCategoryById(id);
        }catch (Exception exception){
            throw new RuntimeException("Category is delete fail");
        }
    }
}
