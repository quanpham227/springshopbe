package com.springshopbe.service.impl;

import com.springshopbe.dto.ProductDTO;
import com.springshopbe.entity.ProductEntity;
import com.springshopbe.service.IProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductService implements IProductService {
    @Override
    public List<ProductDTO> getAllCategories() {
        return null;
    }

    @Override
    public Page<ProductEntity> searchProductPaginged(String name, Pageable pageable) {
        return null;
    }

    @Override
    public ProductDTO findByProductId(Long id) {
        return null;
    }

    @Override
    public ProductDTO findByProductName(String name) {
        return null;
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        return null;
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        return null;
    }

    @Override
    public void deleteProductById(Long id) {

    }
}
