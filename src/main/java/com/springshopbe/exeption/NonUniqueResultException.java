package com.springshopbe.exeption;

public class NonUniqueResultException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public NonUniqueResultException(String message) {
    super(message);
  }
}
