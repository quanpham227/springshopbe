package com.springshopbe.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CategoryExeption extends RuntimeException{
    public CategoryExeption(String message) {
        super(message);
    }
}
