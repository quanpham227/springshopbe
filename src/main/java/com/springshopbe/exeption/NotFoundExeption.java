package com.springshopbe.exeption;

public class NotFoundExeption extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public NotFoundExeption() {

  }

  public NotFoundExeption(String message) {
    super(message);
  }

  public NotFoundExeption(String message, Throwable cause) {
    super(message, cause);
  }
}
