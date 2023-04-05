package com.springshopbe.exeption;

public class DuplicateRecordException extends RuntimeException {

  public DuplicateRecordException(String message) {
    super(message);
  }
}