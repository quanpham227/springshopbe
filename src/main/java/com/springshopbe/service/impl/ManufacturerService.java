package com.springshopbe.service.impl;

import com.springshopbe.dto.ManufacturerDTO;
import com.springshopbe.entity.ManufacturerEntity;
import com.springshopbe.exeption.FileNotFoundExeption;
import com.springshopbe.exeption.ManufacturerExeption;
import com.springshopbe.repository.ManufacturerRepository;
import com.springshopbe.service.IManufacturerServicer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerService implements IManufacturerServicer {
    @Autowired
    private ManufacturerRepository manufacturerRepository;

    @Autowired
    private  FileStogareService fileStogareService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ManufacturerDTO insertManufacturer(ManufacturerDTO manufacturerDTO) {
        List<?> foundedList = manufacturerRepository.findByNameContainsIgnoreCase(manufacturerDTO.getName());
        if(foundedList.size()>0){
            throw new ManufacturerExeption("Manufacturer name is existed");
        }
        ManufacturerEntity manufacturerEntity = new ManufacturerEntity();

        if(manufacturerDTO.getLogoFile() != null){
            String filename = fileStogareService.storeLogoFile(manufacturerDTO.getLogoFile());
            manufacturerEntity = this.modelMapper.map(manufacturerDTO, ManufacturerEntity.class);
            manufacturerEntity.setLogo(filename);
        }else {
            throw new FileNotFoundExeption("Logo file is not found");
        }
        return modelMapper.map(manufacturerRepository.save(manufacturerEntity),ManufacturerDTO.class);
    }
}
