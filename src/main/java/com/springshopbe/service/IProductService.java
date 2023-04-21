package com.springshopbe.service;

import com.springshopbe.dto.ProductBriefDTO;
import com.springshopbe.dto.ProductDTO;
import com.springshopbe.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService {
    List<ProductDTO> getAllCategories();

    Page<ProductEntity> searchProductPaginged(String name, Pageable pageable);

    ProductDTO getEditedProductById(Long id);

    ProductDTO findByProductName(String name);

    ProductDTO createProduct(ProductDTO productDTO);

    ProductDTO updateProduct(Long id, ProductDTO productDTO);

    void deleteProductById(Long id);


    Page<ProductBriefDTO> getProductBriefsByName(String name, Pageable pageable);

}
