package com.springshopbe.controller;

import com.springshopbe.dto.ManufacturerDTO;
import com.springshopbe.service.IManufacturerServicer;
import com.springshopbe.service.impl.FileStogareService;
import com.springshopbe.service.impl.ManufacturerService;
import com.springshopbe.service.impl.MapValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
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
}
