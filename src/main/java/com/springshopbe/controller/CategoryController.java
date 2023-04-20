package com.springshopbe.controller;

import com.springshopbe.dto.CategoryDTO;
import com.springshopbe.entity.CategoryEntity;
import com.springshopbe.service.ICategoryService;
import com.springshopbe.service.impl.MapValidationErrorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin
public class CategoryController {
    @Autowired
    private MapValidationErrorService mapValidationErrorService;
    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories (){
        List<CategoryDTO> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<?> getAllCategories
            (@RequestParam(value = "query", defaultValue = "", required = false) String query,
             @PageableDefault( size = 5, sort = "name", direction = Sort.Direction.ASC) Pageable pageable){

        Page<CategoryEntity> categoryEntityPage = categoryService.searchCategoryPaginged(query, pageable);
        List<CategoryDTO> categoryDTOList = categoryEntityPage.stream().map(item->{
            CategoryDTO categoryDTO = new CategoryDTO();
            BeanUtils.copyProperties(item, categoryDTO);
            return categoryDTO;
        }).collect(Collectors.toList());
        Page<CategoryDTO> categories = new PageImpl<CategoryDTO>(categoryDTOList, categoryEntityPage.getPageable(), categoryEntityPage.getTotalPages());
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/get")
    public ResponseEntity<?> getCategoryById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(categoryService.findByCategoryId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody @Valid CategoryDTO categoryDTO, BindingResult result){
        ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationFieds(result);
        if(responseEntity != null){
            return responseEntity;
        }
        CategoryDTO category = categoryService.createCategory(categoryDTO);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }
    @PatchMapping ("{id}")
    public ResponseEntity<?> updateCategory (@PathVariable ("id") Long id, @RequestBody @Valid CategoryDTO categoryDTO, BindingResult result){
        ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationFieds(result);
        if(responseEntity != null){
            return responseEntity;
        }


        CategoryDTO category = categoryService.updateCategory(id, categoryDTO);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id")  Long id) {
        categoryService.deleteCategoryById(id);
        return new ResponseEntity<>("Category with id "+ id + " was deleted ",HttpStatus.OK);
    }
}
