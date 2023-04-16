package com.springshopbe.service;

import com.springshopbe.dto.ManufacturerDTO;
import com.springshopbe.entity.ManufacturerEntity;
import com.springshopbe.repository.ManufacturerRepository;
import org.springframework.beans.factory.annotation.Autowired;

public interface IManufacturerServicer {


    ManufacturerDTO insertManufacturer(ManufacturerDTO manufacturerDTO);
}