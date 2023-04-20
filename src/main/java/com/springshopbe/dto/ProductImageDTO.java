package com.springshopbe.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class ProductImageDTO extends AbstractDTO<ProductImageDTO> implements Serializable {


    private String name;

    private String uid;

    private String fileName;

    private String url;

    private String status;

    private String response = "{\"status\": \"success\"}";
}
