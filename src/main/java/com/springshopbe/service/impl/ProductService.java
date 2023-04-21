package com.springshopbe.service.impl;

import com.springshopbe.dto.ProductDTO;
import com.springshopbe.dto.ProductImageDTO;
import com.springshopbe.entity.CategoryEntity;
import com.springshopbe.entity.ManufacturerEntity;
import com.springshopbe.entity.ProductEntity;
import com.springshopbe.entity.ProductImageEntity;
import com.springshopbe.repository.ProductImageRepository;
import com.springshopbe.repository.ProductRepository;
import com.springshopbe.service.IProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService implements IProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private FileStogareService fileStogareService;


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
    @Transactional(rollbackFor = Exception.class)
    public ProductDTO createProduct(ProductDTO productDTO) {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(productDTO, productEntity);

        ManufacturerEntity manufacturer = new ManufacturerEntity();
        manufacturer.setId(productDTO.getManufacturerId());
        productEntity.setManufacturer(manufacturer);

        CategoryEntity category = new CategoryEntity();
        category.setId(productDTO.getCategoryId());
        productEntity.setCategory(category);

        if(productDTO.getImage() != null){
            ProductImageEntity img = new ProductImageEntity();
            BeanUtils.copyProperties(productDTO.getImage(), img);
            ProductImageEntity saveImg = productImageRepository.save(img);
            productEntity.setImage(saveImg);
        }
        if(productDTO.getImages() != null && productDTO.getImages().size() > 0){
            Set<ProductImageEntity> productImageEntities = saveProductImages(productDTO);
            productEntity.setImages(productImageEntities);
        }
        ProductEntity savedProduct = productRepository.save(productEntity);
        productDTO.setId(savedProduct.getId());

        return productDTO;
    }

    private Set<ProductImageEntity> saveProductImages(ProductDTO productDTO) {
        Set<ProductImageEntity>  entityList = new HashSet<>();

        List<ProductImageDTO> newList = productDTO.getImages().stream().map(item-> {
            ProductImageEntity img = new ProductImageEntity();
            BeanUtils.copyProperties(item, img);

            ProductImageEntity saveImg = productImageRepository.save(img);
            item.setId(saveImg.getId());

            entityList.add(saveImg);

            return item;
        }).collect(Collectors.toList());

        productDTO.setImages(newList);
        return entityList;
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        return null;
    }

    @Override
    @Transactional
    public void deleteProductById(Long id) {

    }
}
