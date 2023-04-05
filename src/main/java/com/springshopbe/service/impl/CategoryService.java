package com.springshopbe.service.impl;

import com.springshopbe.dto.CategoryDTO;
import com.springshopbe.entity.CategoryEntity;
import com.springshopbe.exeption.BadRequestExeption;
import com.springshopbe.exeption.DuplicateRecordException;
import com.springshopbe.exeption.NotFoundExeption;
import com.springshopbe.repository.CategoryRepository;
import com.springshopbe.service.ICategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
public class CategoryService implements ICategoryService {

    private final ModelMapper modelMapper;

    private final CategoryRepository categoryRepository;


    public CategoryService(ModelMapper modelMapper, CategoryRepository categoryRepository) {
        this.modelMapper = modelMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<CategoryDTO> getAllCategoryPaginged(Pageable pageable) {
        Page<CategoryEntity> categoryEntityPage = categoryRepository.getAllCategoryPagined(pageable);

        return categoryEntityPage.map(new Function<CategoryEntity, CategoryDTO>() {
            @Override
            public CategoryDTO apply(CategoryEntity category) {
                return new ModelMapper().map(category, CategoryDTO.class);
            }
        });
    }

    @Override
    public Page<CategoryDTO> searchCategoryPaginged(String name, Pageable pageable) {
        Page<CategoryEntity> categoryEntityPage = categoryRepository.searchCategoryPaginged(name, pageable);
        return categoryEntityPage.map(new Function<CategoryEntity, CategoryDTO>() {
            @Override
            public CategoryDTO apply(CategoryEntity category) {
                return null;
            }
        });
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
            throw new DuplicateRecordException("Duplicate Record with code: " + categoryDTO.getName());
        }
        try{
            CategoryEntity categoryEntity = modelMapper.map(categoryDTO, CategoryEntity.class);
            return modelMapper.map(categoryRepository.save(categoryEntity), CategoryDTO.class);
        }catch (Exception exception){
            throw new BadRequestExeption("Category is create fail");
        }
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntityOld = this.categoryRepository.findByCategoryId(categoryDTO.getId())
                .orElseThrow(()-> new NotFoundExeption("Category not found with id :" + categoryDTO.getId()));
        try{
            this.modelMapper.map(categoryDTO, categoryEntityOld);
            return modelMapper.map(this.categoryRepository.save(categoryEntityOld), CategoryDTO.class);
        }catch (Exception exception){
            throw new BadRequestExeption("Category is update fail");
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
            throw new BadRequestExeption("Category is delete fail");
        }
    }
}
