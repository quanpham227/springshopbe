package com.springshopbe.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseDTO <T>{

    private Long id;
    @JsonIgnore
    private Timestamp createdDate;
    @JsonIgnore
    private Timestamp modifiedDate;
    @JsonIgnore
    private String createdBy;
    @JsonIgnore
    private String modifiedBy;
}
