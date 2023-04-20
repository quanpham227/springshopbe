package com.springshopbe.service.impl;

import com.springshopbe.dto.ManufacturerDTO;
import com.springshopbe.entity.ManufacturerEntity;
import com.springshopbe.exeption.FileNotFoundExeption;
import com.springshopbe.exeption.ManufacturerExeption;
import com.springshopbe.exeption.NotFoundExeption;
import com.springshopbe.repository.ManufacturerRepository;
import com.springshopbe.service.IManufacturerServicer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        if(manufacturerDTO.getLogoFile().isEmpty()){
            throw new FileNotFoundExeption("Logo file is empty");
        }
        try {
            String filename = fileStogareService.storeLogoFile(manufacturerDTO.getLogoFile());
            manufacturerEntity = this.modelMapper.map(manufacturerDTO, ManufacturerEntity.class);
            manufacturerEntity.setLogo(filename);
            return modelMapper.map(manufacturerRepository.save(manufacturerEntity),ManufacturerDTO.class);
        }catch (Exception exception){
            throw new RuntimeException("create manufacturer is failed");
        }
    }

    @Override
    public ManufacturerDTO updateManufacturer(Long id,ManufacturerDTO manufacturerDTO) {
        ManufacturerEntity manufacturerEntityOld = manufacturerRepository.getManufacturerEntitiesById(id)
                .orElseThrow(() -> new NotFoundExeption("Manufacturer with id: " + id + " not found"));
        try {
            if(manufacturerDTO.getLogoFile() != null) {
                String filename = fileStogareService.storeLogoFile(manufacturerDTO.getLogoFile());
                fileStogareService.deleteLogoFile(manufacturerEntityOld.getLogo());
                manufacturerDTO.setLogo(filename);
            }
            this.modelMapper.map(manufacturerDTO, manufacturerEntityOld);
            return modelMapper.map(manufacturerRepository.save(manufacturerEntityOld), ManufacturerDTO.class);
        }catch (Exception exception){
            throw new RuntimeException("update manufacturer is failed");
        }
    }

    @Override
    public Page<ManufacturerEntity> getAllManufacturers(Pageable pageable) {
        Page<ManufacturerEntity> manufacturers= manufacturerRepository.getAllManufacturersPaginged(pageable);
        return manufacturers;
    }

    @Override
    public Page<ManufacturerDTO> getAllManufacturerPaginged(Pageable pageable) {
        Page<ManufacturerEntity> manufacturerEntityPage = manufacturerRepository.getAllManufacturersPaginged(pageable);
        return manufacturerEntityPage.map(new Function<ManufacturerEntity, ManufacturerDTO>() {
            @Override
            public ManufacturerDTO apply(ManufacturerEntity manufacturerEntity) {
                return new ModelMapper().map(manufacturerEntity, ManufacturerDTO.class);
            }
        });


    }

    @Override
    public Page<ManufacturerDTO> getAllManufacturerPaginged(String query, Pageable pageable) {
        Page<ManufacturerEntity> manufacturerEntityPage = manufacturerRepository.getAllManufacturersPaginged(query,pageable);
        return manufacturerEntityPage.map(new Function<ManufacturerEntity, ManufacturerDTO>() {
            @Override
            public ManufacturerDTO apply(ManufacturerEntity manufacturerEntity) {
                return new ModelMapper().map(manufacturerEntity, ManufacturerDTO.class);
            }
        });

    }

    @Override
    public ManufacturerDTO findById(Long id) {
        ManufacturerEntity manufacturerEntity = manufacturerRepository.getManufacturerEntitiesById(id)
                .orElseThrow(() -> new NotFoundExeption("Manufacturer not found with id :" + id));
        return modelMapper.map(manufacturerEntity, ManufacturerDTO.class);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        ManufacturerEntity manufacturerEntity = manufacturerRepository.getManufacturerEntitiesById(id)
                .orElseThrow(() -> new NotFoundExeption("Manufacturer not found with id :" + id));
        fileStogareService.deleteLogoFile(manufacturerEntity.getLogo());
        manufacturerRepository.deleteManufacturerEntityById(id);

    }
}
