package com.springshopbe.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ManufacturerDTO extends AbstractDTO<ManufacturerDTO> implements Serializable {
    private String name;
    private String logo;

    @JsonIgnore
    private MultipartFile logoFile;
}
