package com.springshopbe.service;

import com.springshopbe.dto.ManufacturerDTO;
import com.springshopbe.entity.ManufacturerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IManufacturerServicer {


    ManufacturerDTO insertManufacturer(ManufacturerDTO manufacturerDTO);
    ManufacturerDTO updateManufacturer(Long id ,ManufacturerDTO manufacturerDTO);

    Page<ManufacturerEntity> getAllManufacturers (Pageable pageable);
    Page<ManufacturerDTO> getAllManufacturerPaginged (Pageable pageable);
    Page<ManufacturerDTO> getAllManufacturerPaginged (String keyword, Pageable pageable);

    ManufacturerDTO findById (Long id);

    void deleteById(Long id);
}