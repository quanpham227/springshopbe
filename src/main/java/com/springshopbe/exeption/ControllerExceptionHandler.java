package com.springshopbe.exeption;

import com.springshopbe.exeption.error.CustomErrorResponse;
import com.springshopbe.exeption.error.ErrorDetails;
import com.springshopbe.exeption.error.ErrorMessageDto;
import com.springshopbe.exeption.error.ErrorResponse;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

  @ExceptionHandler({DuplicateRecordException.class, IllegalArgumentException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handlerDuplicateRecordException(DuplicateRecordException ex,
      WebRequest request) {
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

  @ExceptionHandler(NonUniqueResultException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handlerNonUniqueResultException(NonUniqueResultException ex,
      WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR,
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


  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
    List<ErrorMessageDto> validationErrorDetails = ex.getBindingResult()
        .getAllErrors()
        .stream()
        .map(error -> mapToErrorMessageDto(error))
        .collect(Collectors.toList());

    CustomErrorResponse response = new CustomErrorResponse(status.name(), status.value(),
        validationErrorDetails);
    return new ResponseEntity<>(response, status);
  }

  private ErrorMessageDto mapToErrorMessageDto(ObjectError error) {
    ConstraintViolationImpl<?> source = (ConstraintViolationImpl) error.unwrap(
        ConstraintViolationImpl.class);
    String fieldError = "";
    String rejectedValue = "";
    if (error instanceof FieldError) {
      fieldError = ((FieldError) error).getField();
      rejectedValue = (String) ((FieldError) error).getRejectedValue();
    }
    return new ErrorMessageDto(error.getObjectName(), fieldError, error.getDefaultMessage(),
        rejectedValue);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseBody
  public ResponseEntity<Object> handleConstraintViolationException(Exception ex,
      WebRequest request) {
    ErrorDetails errorDetails = new ErrorDetails(LocalDate.now(), "ConstraintViolationException",
        ex.getMessage());
    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
  }
}
