package com.springshopbe.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springshopbe.entity.ProductStatus;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ProductDTO extends AbstractDTO<ProductDTO> implements Serializable {
    @NotEmpty(message = "Name is required")
    private String name;

    @Min(value = 0)
    private Integer quantity;

    @Min(value = 0)
    private Double price;

    @Max(value = 0)
    private Float discount;


    private Long viewCount;

    private Boolean isFeatured;

    private String brief;

    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date manufactureDate;

    private ProductStatus status;

    private Long categoryId;

    private Long manufacturerId;

    private List<ProductImageDTO> images;

    private ProductImageDTO image;

    private CategoryDTO category;

    private ManufacturerDTO manufacturer;

}
