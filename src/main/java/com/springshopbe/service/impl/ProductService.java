package com.springshopbe.service.impl;

import com.springshopbe.dto.ProductBriefDTO;
import com.springshopbe.dto.ProductDTO;
import com.springshopbe.dto.ProductImageDTO;
import com.springshopbe.entity.CategoryEntity;
import com.springshopbe.entity.ManufacturerEntity;
import com.springshopbe.entity.ProductEntity;
import com.springshopbe.entity.ProductImageEntity;
import com.springshopbe.exeption.NotFoundExeption;
import com.springshopbe.repository.ProductImageRepository;
import com.springshopbe.repository.ProductRepository;
import com.springshopbe.service.IProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    public ProductDTO getEditedProductById(Long id) {
        ProductEntity found = productRepository.findById(id).orElseThrow(() -> new NotFoundExeption("Product not found"));
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(found, productDTO);

        productDTO.setCategoryId(found.getCategory().getId());
        productDTO.setManufacturerId(found.getManufacturer().getId());

        List<ProductImageDTO> images = found.getImages().stream().map(item -> {
            ProductImageDTO productImageDTO = new ProductImageDTO();
            BeanUtils.copyProperties(item, productImageDTO);
            return productImageDTO;
        }).collect(Collectors.toList());

        productDTO.setImages(images);

        ProductImageDTO imageDTO = new ProductImageDTO();
        BeanUtils.copyProperties(found.getImage(), imageDTO);

        productDTO.setImage(imageDTO);

        return productDTO;
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
    @Transactional(rollbackFor = Exception.class)
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        ProductEntity found = productRepository.findById(id)
                .orElseThrow(()-> new NotFoundExeption("Product not found"));

        String ignoreFields[] = new String[]{"createdDate","createBy", "image", "images", "viewCount"};
        BeanUtils.copyProperties(productDTO,found,ignoreFields );

        if(productDTO.getImage().getId() != null && found.getImage().getId() != productDTO.getImage().getId()){
            fileStogareService.deleteProductImageFile(found.getImage().getFileName());

            ProductImageEntity img = new ProductImageEntity();
            BeanUtils.copyProperties(productDTO.getImage(), img);

            productImageRepository.save(img);

            found.setImage(img);
        }


        ManufacturerEntity manuf = new ManufacturerEntity();
        manuf.setId(productDTO.getManufacturerId());
        found.setManufacturer(manuf);

        CategoryEntity cate = new CategoryEntity();
        cate.setId(productDTO.getCategoryId());
        found.setCategory(cate);

        if(productDTO.getImages().size() > 0){
            List<ProductImageEntity> toDeleteFile = new ArrayList<>();

            found.getImages().stream().forEach(item -> {
                Boolean existed = productDTO.getImages().stream().anyMatch(img -> img.getId() == item.getId());

                if(!existed) toDeleteFile.add(item);
            });
            if(toDeleteFile.size() > 0){
                toDeleteFile.stream().forEach(item -> {
                    fileStogareService.deleteProductImageFile(item.getFileName());
                    productImageRepository.delete(item);
                });
            }
            Set<ProductImageEntity> imgList = productDTO.getImages().stream().map(item-> {
                ProductImageEntity img = new ProductImageEntity();
                BeanUtils.copyProperties(item, img);
                return img;
            }).collect(Collectors.toSet());
            found.setImages(imgList);
        }
        ProductEntity savedEntity = productRepository.save(found);
        productDTO.setId(savedEntity.getId());
        return productDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteProductById(Long id) {
        ProductEntity found = productRepository.findById(id)
                .orElseThrow(()-> new NotFoundExeption("Product not found"));

        if(found.getImage() != null){
            fileStogareService.deleteProductImageFile(found.getImage().getFileName());
            productImageRepository.delete(found.getImage());
        }
        if(found.getImages().size() > 0){
            found.getImages().stream().forEach(item -> {
                fileStogareService.deleteProductImageFile(item.getFileName());
                productImageRepository.delete(item);
            });
        }
        productRepository.delete(found);

    }

    @Override
    public Page<ProductBriefDTO> getProductBriefsByName(String name, Pageable pageable) {
        Page<ProductEntity> productEntityPage = productRepository.findByNameContainsIgnoreCase(name, pageable);

        List<ProductBriefDTO> productBriefDTOList = productEntityPage.getContent().stream().map(item -> {
            ProductBriefDTO productBriefDTO = new ProductBriefDTO();
            BeanUtils.copyProperties(item, productBriefDTO);

            productBriefDTO.setCategoryName(item.getCategory().getName());
            productBriefDTO.setManufacturerName(item.getManufacturer().getName());
            productBriefDTO.setImageFileName(item.getImage().getFileName());

            return productBriefDTO;
        }).collect(Collectors.toList());

        Page<ProductBriefDTO> newPage = new PageImpl<ProductBriefDTO>(productBriefDTOList, productEntityPage.getPageable(), productEntityPage.getTotalElements());
        return newPage;
    }
}
