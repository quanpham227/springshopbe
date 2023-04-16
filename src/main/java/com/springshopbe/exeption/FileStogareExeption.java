package com.springshopbe.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FileStogareExeption extends RuntimeException{
    public FileStogareExeption(String message) {
        super(message);
    }

    public FileStogareExeption(String message, Throwable cause) {
        super(message, cause);
    }
}
