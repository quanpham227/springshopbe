package com.springshopbe.exeption.error;


import java.util.List;

public class CustomErrorResponse {

  private String error;
  private int code;
  private List<ErrorMessageDto> errors;

  public CustomErrorResponse() {
  }

  public CustomErrorResponse(String error, int code, List<ErrorMessageDto> errors) {
    this.error = error;
    this.code = code;
    this.errors = errors;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public List<ErrorMessageDto> getErrors() {
    return errors;
  }

  public void setErrors(
      List<ErrorMessageDto> errors) {
    this.errors = errors;
  }
}
