package com.springshopbe.controller;


import com.springshopbe.dto.ProductImageDTO;
import com.springshopbe.dto.UploadedFileInfo;
import com.springshopbe.exeption.FileStogareExeption;
import com.springshopbe.service.IManufacturerServicer;
import com.springshopbe.service.IProductService;
import com.springshopbe.service.impl.FileStogareService;
import com.springshopbe.service.impl.MapValidationErrorService;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin
public class ProductController {
    @Autowired
    private IProductService iProductService;
    @Autowired
    private FileStogareService fileStogareService;
    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping(value = "/images/one",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE,
                    MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                    MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> uploadImage (@RequestParam ("file")MultipartFile imageFile){

        UploadedFileInfo fileInfo = fileStogareService.storeUploadedProductImageFile(imageFile);
        ProductImageDTO dto = new ProductImageDTO();
        BeanUtils.copyProperties(fileInfo, dto);

        dto.setStatus("done");
        dto.setUrl("http://localhost:8080/api/v1/products/images/" + fileInfo.getFileName());
        return new ResponseEntity<>(dto,HttpStatus.CREATED);
    }
}
