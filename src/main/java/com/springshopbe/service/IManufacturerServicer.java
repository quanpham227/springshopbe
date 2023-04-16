package com.springshopbe.service;

import com.springshopbe.dto.ManufacturerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IManufacturerServicer {


    ManufacturerDTO insertManufacturer(ManufacturerDTO manufacturerDTO);
    List<ManufacturerDTO> getAllManufacturer ();
    Page<ManufacturerDTO> getAllManufacturerPaginged (Pageable pageable);
    Page<ManufacturerDTO> getAllManufacturerPaginged (String keyword, Pageable pageable);

    ManufacturerDTO findById (Long id);

    void deleteById(Long id);
}