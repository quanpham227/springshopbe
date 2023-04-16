package com.springshopbe.controller;

import com.springshopbe.dto.ManufacturerDTO;
import com.springshopbe.exeption.FileNotFoundExeption;
import com.springshopbe.exeption.FileStogareExeption;
import com.springshopbe.service.IManufacturerServicer;
import com.springshopbe.service.impl.FileStogareService;
import com.springshopbe.service.impl.ManufacturerService;
import com.springshopbe.service.impl.MapValidationErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping("/logo/{filename:.+}")
    public ResponseEntity<?> downloadFile (@PathVariable String filename, HttpServletRequest request){
        Resource resource = fileStogareService.loadFileAsResource(filename);

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
