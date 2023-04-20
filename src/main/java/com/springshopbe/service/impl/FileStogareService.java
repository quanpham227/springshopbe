package com.springshopbe.service.impl;

import com.springshopbe.config.FileStorageProperties;
import com.springshopbe.dto.UploadedFileInfo;
import com.springshopbe.exeption.FileNotFoundExeption;
import com.springshopbe.exeption.FileStogareExeption;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStogareService {

    private final Path fileLogoStogareLocation;
    private final Path fileProductImageStogareLocation;

    public String storeLogoFile (MultipartFile file){
        return stogareFile(fileLogoStogareLocation, file);
    }
    public String storeProductImageFile (MultipartFile file){
        return stogareFile(fileProductImageStogareLocation, file);
    }
    public UploadedFileInfo storeUploadedProductImageFile (MultipartFile file){
        return stogareUploadedFile(fileProductImageStogareLocation, file);
    }
    public FileStogareService (FileStorageProperties fileStorageProperties) {
        this.fileLogoStogareLocation = Paths.get(fileStorageProperties.getUploadLogoDir()).toAbsolutePath().normalize();
        this.fileProductImageStogareLocation = Paths.get(fileStorageProperties.getUploadProductImageDir()).toAbsolutePath().normalize();

        try {

            Files.createDirectories(fileLogoStogareLocation);
            Files.createDirectories(fileProductImageStogareLocation);

        }catch (Exception exception){
            throw new FileStogareExeption("Counld not create the directory where the upload files will be stored", exception);
        }
    }
    private String stogareFile(Path location, MultipartFile file){
        UUID uudi = UUID.randomUUID();
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String filename = uudi.toString() + "." + ext;
        try {
            if(filename.contains("..")){
                throw new FileStogareExeption("Sorry ! Filename contrains invalid path sequence " + filename);
            }
            Path targetLocation = location.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return filename;
        }catch (Exception exception){
            throw new FileStogareExeption("Could not store file " + filename + ". Please try again !", exception);
        }
    }


    private UploadedFileInfo stogareUploadedFile(Path location, MultipartFile file){
        UUID uudi = UUID.randomUUID();
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String filename = uudi.toString() + "." + ext;
        try {
            if(filename.contains("..")){
                throw new FileStogareExeption("Sorry ! Filename contrains invalid path sequence " + filename);
            }
            Path targetLocation = location.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            UploadedFileInfo info = new UploadedFileInfo();
            info.setFileName(filename);
            info.setUid(Long.valueOf(uudi.toString()));
            info.setName(StringUtils.getFilename(file.getOriginalFilename()));

            return info;
        }catch (Exception exception){
            throw new FileStogareExeption("Could not store file " + filename + ". Please try again !", exception);
        }
    }


    public Resource loadFileAsResource (String filename){
        return loadFileAsResource(fileLogoStogareLocation, filename);
    }

    public Resource loadProductImageFileAsResource (String filename){
        return loadFileAsResource(fileProductImageStogareLocation, filename);
    }
    private Resource loadFileAsResource(Path location, String filename) {
        try {
            Path filePath = location.resolve(filename).normalize();

            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists()){
                return resource;
            }else {
                throw new FileNotFoundExeption("File not found " + filename);
            }
        }catch (Exception exception){
            throw new FileNotFoundExeption("File not found " + filename, exception);
        }
    }




    public void deleteLogoFile(String filename){
        deleteFile(fileLogoStogareLocation, filename);
    }
    public void deleteProductImageFile(String filename){
        deleteFile(fileProductImageStogareLocation, filename);
    }
    private void deleteFile(Path location, String filename){

        try {
            Path filePath = location.resolve(filename).normalize();

            if(!Files.exists(filePath)){
                throw new FileNotFoundExeption("file not found " + filename);
            }
            Files.delete(filePath);
        }catch (Exception exception){
            throw new FileNotFoundExeption("file not found " + filename, exception);
        }
    }

}
