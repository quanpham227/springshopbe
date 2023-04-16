package com.springshopbe.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ManufacturerExeption extends RuntimeException{
    public ManufacturerExeption(String message) {
        super(message);
    }
}
