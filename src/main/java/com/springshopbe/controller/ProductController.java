package com.springshopbe.controller;


import com.springshopbe.dto.ProductDTO;
import com.springshopbe.dto.ProductImageDTO;
import com.springshopbe.dto.UploadedFileInfo;
import com.springshopbe.exeption.FileStogareExeption;
import com.springshopbe.service.IProductService;
import com.springshopbe.service.impl.FileStogareService;
import com.springshopbe.service.impl.MapValidationErrorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin
public class ProductController {
    @Autowired
    private IProductService productService;
    @Autowired
    private FileStogareService fileStogareService;
    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @GetMapping("/find")
    public ResponseEntity<?> getProductBriefsByName(@RequestParam("query") String query,
                                              @PageableDefault(size =10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable){


        return new ResponseEntity<>(productService.getProductBriefsByName(query,pageable), HttpStatus.OK);

    }
    @GetMapping("/{id}/getedit")
    public ResponseEntity<?> getEditedProduct(@PathVariable Long id){
        return new ResponseEntity<>(productService.getEditedProductById(id), HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO dto, BindingResult result){
        ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationFieds(result);

        if(responseEntity != null){
            return responseEntity;
        }

        ProductDTO saveDto = productService.createProduct(dto);

        return new ResponseEntity<>(saveDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/all")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO dto, BindingResult result){
        ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationFieds(result);

        if(responseEntity != null){
            return responseEntity;
        }

        ProductDTO updateDto = productService.updateProduct(id,dto);

        return new ResponseEntity<>(updateDto, HttpStatus.CREATED);
    }

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

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<?> downloadFile (@PathVariable String filename, HttpServletRequest request){

        Resource resource = fileStogareService.loadProductImageFileAsResource(filename);
        String contentType = null;
        try{
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        }catch (Exception exception) {
            throw new FileStogareExeption("Could not determine file type");
        }
        if(contentType == null){
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\""
                        + resource.getFilename() + "\"")
                .body(resource);
    }
}
