package com.springshopbe.controller;

import com.springshopbe.dto.ManufacturerDTO;
import com.springshopbe.entity.ManufacturerEntity;
import com.springshopbe.exeption.FileStogareExeption;
import com.springshopbe.service.IManufacturerServicer;
import com.springshopbe.service.impl.FileStogareService;
import com.springshopbe.service.impl.MapValidationErrorService;
import jdk.nashorn.internal.ir.CallNode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/manufacturers")
public class ManufacturerController {
    @Autowired
    private IManufacturerServicer manufacturerService;
    @Autowired
    private FileStogareService fileStogareService;
    @Autowired
    private MapValidationErrorService mapValidationErrorService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createManufacturer(@Valid @ModelAttribute ManufacturerDTO manufacturerDTO, BindingResult result){
        ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationFieds(result);
        if(responseEntity != null){
            return responseEntity;
        }
        ManufacturerDTO dto = manufacturerService.insertManufacturer(manufacturerDTO);
        return new  ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PatchMapping(value = "{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE,
                MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateManufacturer(@PathVariable Long id ,@Valid @ModelAttribute ManufacturerDTO manufacturerDTO, BindingResult result){
        ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationFieds(result);
        if(responseEntity != null){
            return responseEntity;
        }
        ManufacturerDTO dto = manufacturerService.updateManufacturer(id,manufacturerDTO);
        return new  ResponseEntity<>(dto, HttpStatus.CREATED);
    }


    @GetMapping("/logo/{filename:.+}")
    public ResponseEntity<?> downloadFile (@PathVariable String filename, HttpServletRequest request){
        Resource resource = fileStogareService.loadLogoFileAsResource(filename);

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

    @GetMapping("/find")
    public ResponseEntity<?> getManufacturers(@RequestParam("query") String query,
                @PageableDefault (size = 5, sort = "name", direction = Sort.Direction.ASC) Pageable pageable){
        Page<ManufacturerDTO> manufacturerDTOPage;
        if(query.isEmpty()){
            manufacturerDTOPage = manufacturerService.getAllManufacturerPaginged(pageable);

        }else {
            manufacturerDTOPage = manufacturerService.getAllManufacturerPaginged(query,pageable);
        }
        Page<ManufacturerDTO> newPage =  new PageImpl<ManufacturerDTO>
                (manufacturerDTOPage.getContent(),manufacturerDTOPage.getPageable(),manufacturerDTOPage.getTotalPages());
        System.out.println(manufacturerDTOPage.getTotalPages());
        System.out.print(manufacturerDTOPage.getTotalElements());
        return new ResponseEntity<>(newPage, HttpStatus.OK);

    }

    @GetMapping("/page")
    public ResponseEntity<?> getmanufacturers(@PageableDefault (size = 5, sort = "name", direction = Sort.Direction.ASC) Pageable pageable){

        Page<ManufacturerEntity> manufacturerEntityPage = manufacturerService.getAllManufacturers(pageable);
        List<ManufacturerDTO> manufacturers = manufacturerEntityPage.stream().map(item-> {
            ManufacturerDTO dto = new ManufacturerDTO();
            BeanUtils.copyProperties(item, dto);
            return dto;
        }).collect(Collectors.toList());

        return new ResponseEntity<>(manufacturers, HttpStatus.OK);
    }
    @GetMapping(value = "/{id}/get")
    public ResponseEntity<?> getManufacturerById (@PathVariable("id") Long id) {
        return new ResponseEntity<>(manufacturerService.findById(id), HttpStatus.OK);

    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteManufatere (@PathVariable("id") Long id) {
        manufacturerService.deleteById(id);
        return new ResponseEntity<>("Manufacturer with id : "+ id + " was deleted ", HttpStatus.OK);

    }
}
