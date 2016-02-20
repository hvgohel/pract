package com.dw.pract.exception;

public class IllegalArgumentException extends RuntimeException implements ErrorInfo {
  private String code;

  public IllegalArgumentException() {
    super();
  }

  public IllegalArgumentException(String msg) {
    super(msg);
  }

  public IllegalArgumentException(String code, String msg) {
    super(msg);
    this.code = code;
  }

  @Override
  public String getCode() {
    return code;
  }

}
