package com.springshopbe.controller;

import com.springshopbe.dto.CategoryDTO;
import com.springshopbe.service.ICategoryService;
import com.springshopbe.service.impl.MapValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> getAllCategories (
            @RequestParam(value = "keyword", defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        try {
            Pageable pageable = PageRequest.of(page,size);
            Page<CategoryDTO> categoryPage;
            if(keyword.isEmpty()){
                categoryPage = categoryService.getAllCategoryPaginged(pageable);
            }else {
                categoryPage = categoryService.searchCategoryPaginged(keyword,pageable);
            }
            List<CategoryDTO> categories = categoryPage.getContent();
            Map<String,Object> response = new HashMap<>();
            response.put("categories", categories);
            response.put("currentPage", categoryPage.getTotalElements());
            response.put("totalPage", categoryPage.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception exception){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
