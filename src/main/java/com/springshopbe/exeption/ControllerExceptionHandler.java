package com.springshopbe.exeption;
import com.springshopbe.exeption.error.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(NotFoundExeption.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handlerNotFoundExeption(NotFoundExeption ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.NOT_FOUND,
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
    return errorResponse;
  }

  @ExceptionHandler(DuplicateRecordException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ErrorResponse handlerDuplicateRecordException(DuplicateRecordException ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.CONFLICT,
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
    return errorResponse;
  }
  @ExceptionHandler(NullPointerException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handlerNullPointerException(NullPointerException ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            new Date(),
            ex.getMessage(),
            request.getDescription(false));
    return errorResponse;
  }

  @ExceptionHandler( IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handlerIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            new Date(),
            ex.getMessage(),
            request.getDescription(false));
    return errorResponse;
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handlerMaxUploadSizeExceeded(MaxUploadSizeExceededException ex,
      WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.BAD_REQUEST,
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
    return errorResponse;
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse dataIntegrityViolationException(DataIntegrityViolationException ex,
      WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
    return errorResponse;
  }

  @ExceptionHandler(FileNotFoundExeption.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleFileNotFoundExeption(FileNotFoundExeption ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            new Date(),
            ex.getMessage(),
            request.getDescription(false));
    return errorResponse;
  }
  @ExceptionHandler(FileStogareExeption.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleFileStogareException(FileStogareExeption ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST,
            new Date(),
            ex.getMessage(),
            request.getDescription(false));
    return errorResponse;
  }

  // Xử lý tất cả các exception chưa được khai báo
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handlerException(Exception ex, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
    return errorResponse;
  }


}
