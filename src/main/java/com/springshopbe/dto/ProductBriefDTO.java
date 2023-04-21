package com.springshopbe.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.springshopbe.entity.ProductStatus;
import lombok.Data;


import java.io.Serializable;
import java.util.Date;


@Data
public class ProductBriefDTO  implements Serializable {
    private Long id;
    private String name;
    private Integer quantity;
    private Double price;
    private Float discount;
    private Long viewCount;
    private Boolean isFeatured;
    private String brief;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date manufactureDate;
    private ProductStatus status;
    private String categoryName;
    private String manufacturerName;
    private String imageFileName;




}
